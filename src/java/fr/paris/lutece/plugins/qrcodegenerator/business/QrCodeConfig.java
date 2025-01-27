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

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import fr.paris.lutece.portal.business.file.File;
/**
 * This is the business class for the object QrCodeConfig
 */ 
public class QrCodeConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    @NotEmpty( message = "#i18n{qrcodegenerator.validation.qrcodeconfig.ConfigurationName.notEmpty}" )
    @Size( max = 50 , message = "#i18n{qrcodegenerator.validation.qrcodeconfig.ConfigurationName.size}" ) 
    private String _strConfigurationName;
    
    private ConfigurationType _nConfigurationType;
    
    private File _fileImageLogo;
    
	public enum ConfigurationType {
		URL(0), AGREGATION(1);

		private final int _nValue;

		ConfigurationType(int nValue) {
			_nValue = nValue;
		}

		public int getValue() {
			return _nValue;
		}
		
		public static ConfigurationType valueOf(int nValue) {
			for (ConfigurationType type : ConfigurationType.values()) {
				if (type.getValue() == nValue) {
					return type;
				}
			}
			return null;
		}
	}

    
	public static QrCodeConfig getDefaultQrCodeConfig( ) {
		QrCodeConfig config = new QrCodeConfig( );
		config.setId(-1);
		config.setConfigurationName("Default");
		config.setConfigurationType(ConfigurationType.URL);
		config.setImageLogo(null);
		return config;
	}
	
	/**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the ConfigurationName
     * @return The ConfigurationName
     */
    public String getConfigurationName( )
    {
        return _strConfigurationName;
    }

    /**
     * Sets the ConfigurationName
     * @param strConfigurationName The ConfigurationName
     */ 
    public void setConfigurationName( String strConfigurationName )
    {
        _strConfigurationName = strConfigurationName;
    }
    
    
    /**
     * Returns the ConfigurationType
     * @return The ConfigurationType
     */
    public int getConfigurationType( )
    {
        return _nConfigurationType == null ? -1 : _nConfigurationType.getValue();
    }
    
    public ConfigurationType getType( )
    {
        return _nConfigurationType;
    }

    /**
     * Sets the ConfigurationType
     * @param nConfigurationType The ConfigurationType
     */ 
    public void setConfigurationType( int nConfigurationType )
    {
        _nConfigurationType = ConfigurationType.valueOf(nConfigurationType);
    }
    
    /**
     * Sets the ConfigurationType
     * @param nConfigurationType The ConfigurationType
     */ 
    public void setConfigurationType( ConfigurationType nConfigurationType )
    {
        _nConfigurationType = nConfigurationType;
    }
    
    
    /**
     * Returns the ImageLogo
     * @return The ImageLogo
     */
    public File getImageLogo( )
    {
        return _fileImageLogo;
    }

    /**
     * Sets the ImageLogo
     * @param fileImageLogo The ImageLogo
     */ 
    public void setImageLogo( File fileImageLogo )
    {
        _fileImageLogo = fileImageLogo;
    }
    
}
