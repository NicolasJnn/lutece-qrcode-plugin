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
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.qrcodegenerator.web;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.LuteceTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import java.io.IOException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import java.util.List;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import java.util.ArrayList;
import org.apache.commons.fileupload.FileItem;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import fr.paris.lutece.plugins.qrcodegenerator.business.QrCodeConfig;
import fr.paris.lutece.plugins.qrcodegenerator.business.QrCodeConfigHome;
/**
 * This is the business class test for the object QrCodeConfig
 */
public class QrCodeConfigXPageTest extends LuteceTestCase
{
    private static final String CONFIGURATIONNAME1 = "ConfigurationName1";
    private static final String CONFIGURATIONNAME2 = "ConfigurationName2";
    private static final int CONFIGURATIONTYPE1 = 1;
    private static final int CONFIGURATIONTYPE2 = 2;
	private static final File IMAGELOGO1 = new File( );
    private static final File IMAGELOGO2 = new File( );

public void testXPage(  ) throws AccessDeniedException, IOException
	{
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
		MockHttpServletResponse response = new MockHttpServletResponse( );
		MockServletConfig config = new MockServletConfig( );

		QrCodeConfigXPage xpage = new QrCodeConfigXPage( );
		assertNotNull( xpage.getCreateQrCodeConfig( request ) );
		
		request = new MockHttpServletRequest();
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createQrCodeConfig" ));
		
		LocalVariables.setLocal(config, request, response);
		
		Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "token", new String [ ] {
        		SecurityTokenService.getInstance( ).getToken( request, "createQrCodeConfig" )
        } );
        parameters.put( "action", new String [ ] {
        		"createQrCodeConfig"
        } );
        parameters.put( "configuration_name", new String [ ] {
        CONFIGURATIONNAME1
        } );
        parameters.put( "configuration_type", new String [ ] {
        String.valueOf( CONFIGURATIONTYPE1)
        } );
        
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        
        List<FileItem> items = new ArrayList<>( );
        
        FileItem image_logo = new DiskFileItemFactory( ).createItem( "image_logo", "text/plain", true, "image_logo" );
        image_logo.getOutputStream( ).write( "something".getBytes( ) );
        items.add( image_logo );
        multipartFiles.put( "image_logo", items );

        MultipartHttpServletRequest requestMultipart = new MultipartHttpServletRequest(request, multipartFiles, parameters);

        assertNotNull( xpage.doCreateQrCodeConfig( requestMultipart ) );
		
		
		//modify QrCodeConfig	
		List<Integer> listIds = QrCodeConfigHome.getIdQrCodeConfigsList(); 

		assertTrue( !listIds.isEmpty( ) );

		request = new MockHttpServletRequest();
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

		assertNotNull( xpage.getModifyQrCodeConfig( request ) );

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		
		parameters = new HashMap<>( );
        parameters.put( "token", new String [ ] {
        		SecurityTokenService.getInstance( ).getToken( request, "modifyQrCodeConfig" )
        } );
        parameters.put( "id", new String [ ] {
        		String.valueOf( listIds.get( 0 ) )
        } );
        parameters.put( "configuration_name", new String [ ] {
        CONFIGURATIONNAME1
        } );
        parameters.put( "configuration_type", new String [ ] {
        String.valueOf( CONFIGURATIONTYPE1)
        } );

        requestMultipart = new MultipartHttpServletRequest(request, new HashMap<>( ), parameters);

        assertNotNull( xpage.doModifyQrCodeConfig( requestMultipart ) );

		//do confirm remove QrCodeConfig
		request = new MockHttpServletRequest();
		request.addParameter( "action" , "confirmRemoveQrCodeConfig" );
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemoveQrCodeConfig" ));;
		request.setMethod("GET");

		try
		{
			xpage.getConfirmRemoveQrCodeConfig( request );
		}
		catch(Exception e)
		{
			assertTrue(e instanceof SiteMessageException);
		}

		//do remove QrCodeConfig
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		request.addParameter( "action" , "removeQrCodeConfig" );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeQrCodeConfig" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doRemoveQrCodeConfig( request ) );

    }
    
}
