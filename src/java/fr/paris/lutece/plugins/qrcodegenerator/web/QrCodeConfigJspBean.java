/*
 * Copyright (c) 2002-2025, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
 	
 
package fr.paris.lutece.plugins.qrcodegenerator.web;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.plugins.qrcodegenerator.business.QrCodeConfig;
import fr.paris.lutece.plugins.qrcodegenerator.business.QrCodeConfigHome;
import fr.paris.lutece.plugins.qrcodegenerator.web.AbstractPaginatorJspBean;

/**
 * This class provides the user interface to manage QrCodeConfig features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageQrCodeConfigs.jsp", controllerPath = "jsp/admin/plugins/qrcodegenerator/", right = "QRCODEGENERATOR_MANAGE_CONFIG" )
public class QrCodeConfigJspBean extends AbstractPaginatorJspBean <Integer, QrCodeConfig>
{

	// Rights
	public static final String RIGHT_MANAGEQRCODECONFIG = "QRCODEGENERATOR_MANAGE_CONFIG";
		
    // Templates
    private static final String TEMPLATE_MANAGE_QRCODECONFIGS = "/admin/plugins/qrcodegenerator/manage_qrcodeconfigs.html";
    private static final String TEMPLATE_CREATE_QRCODECONFIG = "/admin/plugins/qrcodegenerator/create_qrcodeconfig.html";
    private static final String TEMPLATE_MODIFY_QRCODECONFIG = "/admin/plugins/qrcodegenerator/modify_qrcodeconfig.html";

    // Parameters
    private static final String PARAMETER_ID_QRCODECONFIG = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_QRCODECONFIGS = "qrcodegenerator.manage_qrcodeconfigs.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_QRCODECONFIG = "qrcodegenerator.modify_qrcodeconfig.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_QRCODECONFIG = "qrcodegenerator.create_qrcodeconfig.pageTitle";

    // Markers
    private static final String MARK_QRCODECONFIG_LIST = "qrcodeconfig_list";
    private static final String MARK_QRCODECONFIG = "qrcodeconfig";
    private static final String MARK_QRCODECONFIGURATION_TYPE_MAP = "qrcodeconfig_type_map";
    private static final String MARK_QRCODECONFIGURATION_TYPE_LIST = "qrcodeconfig_type_list";
    private static final String MARK_QRCODECONFIGURATION_TYPE = "configuration_type";

    private static final String JSP_MANAGE_QRCODECONFIGS = "jsp/admin/plugins/qrcodegenerator/ManageQrCodeConfigs.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_QRCODECONFIG = "qrcodegenerator.message.confirmRemoveQrCodeConfig";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "qrcodegenerator.model.entity.qrcodeconfig.attribute.";

    // Views
    private static final String VIEW_MANAGE_QRCODECONFIGS = "manageQrCodeConfigs";
    private static final String VIEW_CREATE_QRCODECONFIG = "createQrCodeConfig";
    private static final String VIEW_MODIFY_QRCODECONFIG = "modifyQrCodeConfig";

    // Actions
    private static final String ACTION_CREATE_QRCODECONFIG = "createQrCodeConfig";
    private static final String ACTION_MODIFY_QRCODECONFIG = "modifyQrCodeConfig";
    private static final String ACTION_REMOVE_QRCODECONFIG = "removeQrCodeConfig";
    private static final String ACTION_CONFIRM_REMOVE_QRCODECONFIG = "confirmRemoveQrCodeConfig";

    // Infos
    private static final String INFO_QRCODECONFIG_CREATED = "qrcodegenerator.info.qrcodeconfig.created";
    private static final String INFO_QRCODECONFIG_UPDATED = "qrcodegenerator.info.qrcodeconfig.updated";
    private static final String INFO_QRCODECONFIG_REMOVED = "qrcodegenerator.info.qrcodeconfig.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private QrCodeConfig _qrcodeconfig;
    private List<Integer> _listIdQrCodeConfigs;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_QRCODECONFIGS, defaultView = true )
    public String getManageQrCodeConfigs( HttpServletRequest request )
    {
        _qrcodeconfig = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdQrCodeConfigs.isEmpty( ) )
        {
        	_listIdQrCodeConfigs = QrCodeConfigHome.getIdQrCodeConfigsList(  );
        }
        Map<String, Object> model = getPaginatedListModel( request, MARK_QRCODECONFIG_LIST, _listIdQrCodeConfigs, JSP_MANAGE_QRCODECONFIGS );
        model.put(MARK_QRCODECONFIGURATION_TYPE_LIST, QrCodeConfig.ConfigurationType.values());

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_QRCODECONFIGS, TEMPLATE_MANAGE_QRCODECONFIGS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<QrCodeConfig> getItemsFromIds( List<Integer> listIds ) 
	{
		List<QrCodeConfig> listQrCodeConfig = QrCodeConfigHome.getQrCodeConfigsListByIds( listIds );
        for (QrCodeConfig fil : listQrCodeConfig)
        {
        	IFileStoreServiceProvider fileStoreService = QrCodeConfigHome.getFileStoreServiceProvider( );
			if ( fil.getImageLogo( ) != null )
			{
			try
				{
					File localFile = fileStoreService.getFileMetaData( fil.getImageLogo( ).getFileKey( ) );
					if ( localFile != null )
					{
						fil.setImageLogo( localFile );
						String strFileUrl = fileStoreService.getFileDownloadUrlBO( localFile.getFileKey( ) );
						fil.getImageLogo( ).setUrl( strFileUrl );
					}
				}
	            catch (Exception e) 
	            {
	            	AppLogService.error( e );
	            	throw new AppException(e.getMessage(), e);
	            }
			}
        }
		
		// keep original order
        return listQrCodeConfig.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
	
	@Override
	int getPluginDefaultNumberOfItemPerPage( ) {
		return AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
	}
    
    /**
    * reset the _listIdQrCodeConfigs list
    */
    public void resetListId( )
    {
    	_listIdQrCodeConfigs = new ArrayList<>( );
    }

    /**
     * Returns the form to create a qrcodeconfig
     *
     * @param request The Http request
     * @return the html code of the qrcodeconfig form
     */
    @View( VIEW_CREATE_QRCODECONFIG )
    public String getCreateQrCodeConfig( HttpServletRequest request )
    {
        _qrcodeconfig = ( _qrcodeconfig != null ) ? _qrcodeconfig : new QrCodeConfig(  );
        Map<String, Integer> configurationTypeMap = new HashMap<String, Integer>( );
        List<String> configurationTypeList = new ArrayList<String>( );
		for (QrCodeConfig.ConfigurationType type : QrCodeConfig.ConfigurationType.values()) {
			configurationTypeMap.put(type.name(), type.getValue());
			configurationTypeList.add(type.name());
		}

        Map<String, Object> model = getModel(  );
        model.put( MARK_QRCODECONFIG, _qrcodeconfig );
        model.put( MARK_QRCODECONFIGURATION_TYPE_MAP, configurationTypeMap );
        model.put( MARK_QRCODECONFIGURATION_TYPE_LIST, configurationTypeList );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_QRCODECONFIG ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_QRCODECONFIG, TEMPLATE_CREATE_QRCODECONFIG, model );
    }

    /**
     * Process the data capture form of a new qrcodeconfig
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_QRCODECONFIG )
    public String doCreateQrCodeConfig( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _qrcodeconfig, request, getLocale( ) );
        
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		IFileStoreServiceProvider fileStoreService = QrCodeConfigHome.getFileStoreServiceProvider( );
        FileItem image_logo = multipartRequest.getFile( "image_logo" );
        QrCodeConfig.ConfigurationType config_type = QrCodeConfig.ConfigurationType.valueOf( Integer.parseInt( request.getParameter( MARK_QRCODECONFIGURATION_TYPE ) ) );
        _qrcodeconfig.setConfigurationType(config_type);
      
        if ( image_logo != null && image_logo.getSize( ) > 0 )
        {
            try
            {
                String strFileStoreKey = fileStoreService.storeFileItem( image_logo );
                File localFile = new File( );
                localFile.setFileKey( strFileStoreKey );
                _qrcodeconfig.setImageLogo( localFile );
            }
            catch (Exception e) 
            {
            	AppLogService.error( "Erreur de stockage du fichier", e );
                throw new AppException( "Erreur de stockage du fichier", e );
            }
        }

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_QRCODECONFIG ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _qrcodeconfig, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_QRCODECONFIG );
        }

        QrCodeConfigHome.create( _qrcodeconfig );
        addInfo( INFO_QRCODECONFIG_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_QRCODECONFIGS );
    }

    /**
     * Manages the removal form of a qrcodeconfig whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_QRCODECONFIG )
    public String getConfirmRemoveQrCodeConfig( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_QRCODECONFIG ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_QRCODECONFIG ) );
        url.addParameter( PARAMETER_ID_QRCODECONFIG, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_QRCODECONFIG, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a qrcodeconfig
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage qrcodeconfigs
     */
    @Action( ACTION_REMOVE_QRCODECONFIG )
    public String doRemoveQrCodeConfig( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_QRCODECONFIG ) );
        
        if ( _qrcodeconfig == null || ( _qrcodeconfig.getId(  ) != nId ) )
        {
            Optional<QrCodeConfig> optQrCodeConfig = QrCodeConfigHome.findByPrimaryKey( nId );
            _qrcodeconfig = optQrCodeConfig.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }
        IFileStoreServiceProvider fileStoreService = QrCodeConfigHome.getFileStoreServiceProvider( );
	    if ( _qrcodeconfig.getImageLogo( ) != null && StringUtils.isNotEmpty( _qrcodeconfig.getImageLogo( ).getFileKey( ) ) )
	    {
	    	try
	        {
        		fileStoreService.delete( _qrcodeconfig.getImageLogo( ).getFileKey( ) );
        	}
            catch (Exception e) 
            {
            	AppLogService.error( e );
            	throw new AppException(e.getMessage(), e);
            }
        }
        
        QrCodeConfigHome.remove( nId );
        addInfo( INFO_QRCODECONFIG_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_QRCODECONFIGS );
    }

    /**
     * Returns the form to update info about a qrcodeconfig
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_QRCODECONFIG )
    public String getModifyQrCodeConfig( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_QRCODECONFIG ) );

        if ( _qrcodeconfig == null || ( _qrcodeconfig.getId(  ) != nId ) )
        {
            Optional<QrCodeConfig> optQrCodeConfig = QrCodeConfigHome.findByPrimaryKey( nId );
            _qrcodeconfig = optQrCodeConfig.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }

       	IFileStoreServiceProvider fileStoreService = QrCodeConfigHome.getFileStoreServiceProvider( );
		if ( _qrcodeconfig.getImageLogo( ) != null && StringUtils.isNotEmpty( _qrcodeconfig.getImageLogo( ).getFileKey( ) ) )
		{
			try
            {
				File localFile = fileStoreService.getFileMetaData( _qrcodeconfig.getImageLogo( ).getFileKey( ) );
				if ( localFile != null )
				{
					_qrcodeconfig.setImageLogo( localFile );
					String strFileUrl = fileStoreService.getFileDownloadUrlBO( localFile.getFileKey( ) );
					_qrcodeconfig.getImageLogo( ).setUrl( strFileUrl );
				}
			}
            catch (Exception e) 
            {
            	AppLogService.error( e );
            	throw new AppException(e.getMessage(), e);
            }
		}

		Map<String, Integer> configurationTypeMap = new HashMap<String, Integer>( );
        List<String> configurationTypeList = new ArrayList<String>( );
		for (QrCodeConfig.ConfigurationType type : QrCodeConfig.ConfigurationType.values()) {
			configurationTypeMap.put(type.name(), type.getValue());
			configurationTypeList.add(type.name());
		}
		
        Map<String, Object> model = getModel(  );
        model.put( MARK_QRCODECONFIG, _qrcodeconfig );
        model.put( MARK_QRCODECONFIGURATION_TYPE_MAP, configurationTypeMap );
        model.put( MARK_QRCODECONFIGURATION_TYPE_LIST, configurationTypeList );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_QRCODECONFIG ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_QRCODECONFIG, TEMPLATE_MODIFY_QRCODECONFIG, model );
    }

    /**
     * Process the change form of a qrcodeconfig
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_QRCODECONFIG )
    public String doModifyQrCodeConfig( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _qrcodeconfig, request, getLocale( ) );
		
		IFileStoreServiceProvider fileStoreService = QrCodeConfigHome.getFileStoreServiceProvider( );
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileItem image_logo = multipartRequest.getFile( "image_logo" );
       	_qrcodeconfig.getImageLogo( ).setFileKey( request.getParameter( "image_logoKey" ) );
       	QrCodeConfig.ConfigurationType config_type = QrCodeConfig.ConfigurationType.valueOf( Integer.parseInt( request.getParameter( MARK_QRCODECONFIGURATION_TYPE ) ) );
        _qrcodeconfig.setConfigurationType(config_type);
      
        if ( image_logo != null && image_logo.getSize( ) > 0 )
        {	
            try
            {
            	fileStoreService.delete( _qrcodeconfig.getImageLogo( ).getFileKey( ) );
                String strFileStoreKey = fileStoreService.storeFileItem( image_logo );
                File localFile = new File( );
                localFile.setFileKey( strFileStoreKey );
                _qrcodeconfig.setImageLogo( localFile );
            }
            catch (Exception e) 
            {
            	AppLogService.error( "Erreur de stockage du fichier", e );
                throw new AppException( "Erreur de stockage du fichier", e );
            }
        }
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_QRCODECONFIG ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _qrcodeconfig, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_QRCODECONFIG, PARAMETER_ID_QRCODECONFIG, _qrcodeconfig.getId( ) );
        }

        QrCodeConfigHome.update( _qrcodeconfig );
        addInfo( INFO_QRCODECONFIG_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_QRCODECONFIGS );
    }
}
