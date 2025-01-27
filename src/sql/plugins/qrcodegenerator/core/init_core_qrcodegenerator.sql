
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'QRCODEGENERATOR_MANAGE_CONFIG';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('QRCODEGENERATOR_MANAGE_CONFIG','qrcodegenerator.adminFeature.ManageQRcodeConfig.name',1,'jsp/admin/plugins/qrcodegenerator/ManageQrCodeConfigs.jsp','qrcodegenerator.adminFeature.ManageQRcodeConfig.description',0,'qrcodegenerator',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'QRCODEGENERATOR_MANAGE_CONFIG';
INSERT INTO core_user_right (id_right,id_user) VALUES ('QRCODEGENERATOR_MANAGE_CONFIG',1);

