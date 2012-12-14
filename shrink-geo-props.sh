cd lib
mv geogebra_properties.jar xgeogebra_properties.jar
unzip xgeogebra_properties.jar
zip geogebra_properties.jar geogebra/properties/colors.properties
zip geogebra_properties.jar geogebra/properties/colors_sv.properties
zip geogebra_properties.jar geogebra/properties/command.properties
zip geogebra_properties.jar geogebra/properties/command_sv.properties
zip geogebra_properties.jar geogebra/properties/error.properties
zip geogebra_properties.jar geogebra/properties/error_sv.properties
zip geogebra_properties.jar geogebra/properties/javaui.properties
zip geogebra_properties.jar geogebra/properties/javaui_sv.properties
zip geogebra_properties.jar geogebra/properties/menu.properties
zip geogebra_properties.jar geogebra/properties/menu_sv.properties
zip geogebra_properties.jar geogebra/properties/plain.properties
zip geogebra_properties.jar geogebra/properties/plain_sv.properties
zip geogebra_properties.jar geogebra/properties/symbols.properties
zip geogebra_properties.jar geogebra/properties/symbols_sv.properties
rm xgeogebra_properties.jar
rm -rf geogebra
rm -rf META-INF
cd ..