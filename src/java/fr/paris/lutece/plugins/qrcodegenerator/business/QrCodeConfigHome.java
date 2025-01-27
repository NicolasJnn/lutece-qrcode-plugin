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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import org.apache.commons.fileupload.FileItem;
import fr.paris.lutece.portal.business.file.File;


import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for QrCodeConfig objects
 */
public final class QrCodeConfigHome
{
    // Static variable pointed at the DAO instance
    private static IQrCodeConfigDAO _dao = SpringContextService.getBean( "qrcodegenerator.qrCodeConfigDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "qrcodegenerator" );
	private static IFileStoreServiceProvider _fileStoreService = FileService.getInstance( ).getFileStoreServiceProvider( );

    /**
     * Private constructor - this class need not be instantiated
     */
    private QrCodeConfigHome(  )
    {
    }

    /**
     * Create an instance of the qrCodeConfig class
     * @param qrCodeConfig The instance of the QrCodeConfig which contains the informations to store
     * @return The  instance of qrCodeConfig which has been created with its primary key.
     */
    public static QrCodeConfig create( QrCodeConfig qrCodeConfig )
    {
        _dao.insert( qrCodeConfig, _plugin );

        return qrCodeConfig;
    }

    /**
     * Update of the qrCodeConfig which is specified in parameter
     * @param qrCodeConfig The instance of the QrCodeConfig which contains the data to store
     * @return The instance of the  qrCodeConfig which has been updated
     */
    public static QrCodeConfig update( QrCodeConfig qrCodeConfig )
    {
        _dao.store( qrCodeConfig, _plugin );

        return qrCodeConfig;
    }

    /**
     * Remove the qrCodeConfig whose identifier is specified in parameter
     * @param nKey The qrCodeConfig Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a qrCodeConfig whose identifier is specified in parameter
     * @param nKey The qrCodeConfig primary key
     * @return an instance of QrCodeConfig
     */
    public static Optional<QrCodeConfig> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the qrCodeConfig objects and returns them as a list
     * @return the list which contains the data of all the qrCodeConfig objects
     */
    public static List<QrCodeConfig> getQrCodeConfigsList( )
    {
        return _dao.selectQrCodeConfigsList( _plugin );
    }
    
    /**
     * Load the id of all the qrCodeConfig objects and returns them as a list
     * @return the list which contains the id of all the qrCodeConfig objects
     */
    public static List<Integer> getIdQrCodeConfigsList( )
    {
        return _dao.selectIdQrCodeConfigsList( _plugin );
    }
    
    /**
     * Load the data of all the qrCodeConfig objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the qrCodeConfig objects
     */
    public static ReferenceList getQrCodeConfigsReferenceList( )
    {
        return _dao.selectQrCodeConfigsReferenceList( _plugin );
    }
    
    /**
     * Load the filteStoreService of the qrCodeConfig objects and returns them as a IFileStoreServiceProvider
     * @return the filteStoreService of the qrCodeConfig object
     */
	public static IFileStoreServiceProvider getFileStoreServiceProvider( )
    {
		return _fileStoreService;
    }
	
    /**
     * Load the data of all the avant objects and returns them as a list
     * @param listIds liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<QrCodeConfig> getQrCodeConfigsListByIds( List<Integer> listIds )
    {
        return _dao.selectQrCodeConfigsListByIds( _plugin, listIds );
    }

}

