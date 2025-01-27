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


package fr.paris.lutece.plugins.qrcodegenerator.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import fr.paris.lutece.portal.business.file.File;
import java.util.Optional;

/**
 * This class provides Data Access methods for QrCodeConfig objects
 */
public final class QrCodeConfigDAO implements IQrCodeConfigDAO
{
    // Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO qrcodegenerator_config ( configuration_name, configuration_type, image_logo ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM qrcodegenerator_config WHERE id_qr_code_config = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE qrcodegenerator_config SET configuration_name = ?, configuration_type = ?, image_logo = ? WHERE id_qr_code_config = ?";
   
	private static final String SQL_QUERY_SELECTALL = "SELECT id_qr_code_config, configuration_name, configuration_type, image_logo FROM qrcodegenerator_config";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_qr_code_config FROM qrcodegenerator_config";

    private static final String SQL_QUERY_SELECTALL_BY_IDS = SQL_QUERY_SELECTALL + " WHERE id_qr_code_config IN (  ";
	private static final String SQL_QUERY_SELECT_BY_ID = SQL_QUERY_SELECTALL + " WHERE id_qr_code_config = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( QrCodeConfig qrCodeConfig, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , qrCodeConfig.getConfigurationName( ) );
            daoUtil.setInt( nIndex++ , qrCodeConfig.getConfigurationType( ) );
            if ( qrCodeConfig.getImageLogo( ) != null )
		    {
		    	daoUtil.setString( nIndex++ , qrCodeConfig.getImageLogo( ).getFileKey( ) );
		    }
		    else
		    {
		    	daoUtil.setString( nIndex++ , null );
		    }
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                qrCodeConfig.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<QrCodeConfig> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        QrCodeConfig qrCodeConfig = null;
	
	        if ( daoUtil.next( ) )
	        {
	            qrCodeConfig = loadFromDaoUtil( daoUtil );
	        }
	
	        return Optional.ofNullable( qrCodeConfig );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( QrCodeConfig qrCodeConfig, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
            	daoUtil.setString( nIndex++ , qrCodeConfig.getConfigurationName( ) );
            	daoUtil.setInt( nIndex++ , qrCodeConfig.getConfigurationType( ) );
	        if ( qrCodeConfig.getImageLogo( ) != null )
		    {
		    	daoUtil.setString( nIndex++ , qrCodeConfig.getImageLogo( ).getFileKey( ) );
		    }
		    else
		    {
		    	daoUtil.setString( nIndex++ , null );
		    }
	        daoUtil.setInt( nIndex , qrCodeConfig.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<QrCodeConfig> selectQrCodeConfigsList( Plugin plugin )
    {
        List<QrCodeConfig> qrCodeConfigList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
				qrCodeConfigList.add( loadFromDaoUtil( daoUtil ) );
	        }
	
	        return qrCodeConfigList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdQrCodeConfigsList( Plugin plugin )
    {
        List<Integer> qrCodeConfigList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            qrCodeConfigList.add( daoUtil.getInt( 1 ) );
	        }
	
	        return qrCodeConfigList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectQrCodeConfigsReferenceList( Plugin plugin )
    {
        ReferenceList qrCodeConfigList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            qrCodeConfigList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        return qrCodeConfigList;
    	}
    }
    
    /**
     * {@inheritDoc }
     */
	@Override
	public List<QrCodeConfig> selectQrCodeConfigsListByIds( Plugin plugin, List<Integer> listIds ) {
		List<QrCodeConfig> qrCodeConfigList = new ArrayList<>(  );
		
		StringBuilder builder = new StringBuilder( );

		if ( !listIds.isEmpty( ) )
		{
			for( int i = 0 ; i < listIds.size(); i++ ) {
			    builder.append( "?," );
			}
	
			String placeHolders =  builder.deleteCharAt( builder.length( ) -1 ).toString( );
			String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";
			
			
	        try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
	        {
	        	int index = 1;
				for( Integer n : listIds ) {
					daoUtil.setInt(  index++, n ); 
				}
	        	
	        	daoUtil.executeQuery(  );
	        	while ( daoUtil.next(  ) )
		        {
		            qrCodeConfigList.add( loadFromDaoUtil( daoUtil ) );
		        }
	        }
	    }
		return qrCodeConfigList;
		
	}


	private QrCodeConfig loadFromDaoUtil (DAOUtil daoUtil) {
		
		QrCodeConfig qrCodeConfig = new QrCodeConfig(  );
		int nIndex = 1;
		
		qrCodeConfig.setId( daoUtil.getInt( nIndex++ ) );
		qrCodeConfig.setConfigurationName( daoUtil.getString( nIndex++ ) );
		qrCodeConfig.setConfigurationType( daoUtil.getInt( nIndex++ ) );
		File fileImageLogo = new File();
		fileImageLogo.setFileKey( daoUtil.getString( nIndex ) );
		qrCodeConfig.setImageLogo( fileImageLogo );
		
		return qrCodeConfig;
	}
}
