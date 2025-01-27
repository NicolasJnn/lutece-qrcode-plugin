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
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.qrcodegenerator.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Optional;

import fr.paris.lutece.portal.business.file.File;

/**
 * This is the business class test for the object QrCodeConfig
 */
public class QrCodeConfigBusinessTest extends LuteceTestCase
{
    private static final String CONFIGURATIONNAME1 = "ConfigurationName1";
    private static final String CONFIGURATIONNAME2 = "ConfigurationName2";
    private static final int CONFIGURATIONTYPE1 = 1;
    private static final int CONFIGURATIONTYPE2 = 2;
	private static final File IMAGELOGO1 = new File( );
    private static final File IMAGELOGO2 = new File( );

	/**
	* test QrCodeConfig
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        QrCodeConfig qrCodeConfig = new QrCodeConfig();
        qrCodeConfig.setConfigurationName( CONFIGURATIONNAME1 );
        qrCodeConfig.setConfigurationType( CONFIGURATIONTYPE1 );
        qrCodeConfig.setImageLogo( IMAGELOGO1 );

        // Create test
        QrCodeConfigHome.create( qrCodeConfig );
        Optional<QrCodeConfig> optQrCodeConfigStored = QrCodeConfigHome.findByPrimaryKey( qrCodeConfig.getId( ) );
        QrCodeConfig qrCodeConfigStored = optQrCodeConfigStored.orElse( new QrCodeConfig ( ) );
        assertEquals( qrCodeConfigStored.getConfigurationName( ) , qrCodeConfig.getConfigurationName( ) );
        assertEquals( qrCodeConfigStored.getConfigurationType( ) , qrCodeConfig.getConfigurationType( ) );
        assertEquals( qrCodeConfigStored.getImageLogo( ).getFileKey( ) , qrCodeConfig.getImageLogo( ).getFileKey( ) );

        // Update test
        qrCodeConfig.setConfigurationName( CONFIGURATIONNAME2 );
        qrCodeConfig.setConfigurationType( CONFIGURATIONTYPE2 );
        qrCodeConfig.setImageLogo( IMAGELOGO2 );
        QrCodeConfigHome.update( qrCodeConfig );
        optQrCodeConfigStored = QrCodeConfigHome.findByPrimaryKey( qrCodeConfig.getId( ) );
        qrCodeConfigStored = optQrCodeConfigStored.orElse( new QrCodeConfig ( ) );
        
        assertEquals( qrCodeConfigStored.getConfigurationName( ) , qrCodeConfig.getConfigurationName( ) );
        assertEquals( qrCodeConfigStored.getConfigurationType( ) , qrCodeConfig.getConfigurationType( ) );
        assertEquals( qrCodeConfigStored.getImageLogo( ).getFileKey( ) , qrCodeConfig.getImageLogo( ).getFileKey( ) );

        // List test
        QrCodeConfigHome.getQrCodeConfigsList( );

        // Delete test
        QrCodeConfigHome.remove( qrCodeConfig.getId( ) );
        optQrCodeConfigStored = QrCodeConfigHome.findByPrimaryKey( qrCodeConfig.getId( ) );
        qrCodeConfigStored = optQrCodeConfigStored.orElse( null );
        assertNull( qrCodeConfigStored );
        
    }
    
    
     

}