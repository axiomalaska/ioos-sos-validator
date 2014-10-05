#!/bin/bash
BASE_URL=https://raw.githubusercontent.com/ioos/sos-guidelines/master/template/milestone1.0
DIR=`dirname $0`
wget -O $DIR/M1_0_CAPABILITIES.xml $BASE_URL/SOS-GetCapabilities.xml
wget -O $DIR/M1_0_SENSOR_ML_NETWORK.xml $BASE_URL/SML-DescribeSensor-Network.xml
wget -O $DIR/M1_0_SENSOR_ML_STATION.xml $BASE_URL/SML-DescribeSensor-Station.xml
wget -O $DIR/M1_0_OBSERVATION_COLLECTION.xml $BASE_URL/OM-GetObservation.xml
wget -O $DIR/M1_0_SWE_TIME_SERIES.xml $BASE_URL/SWE-MultiStation-TimeSeries.xml
wget -O $DIR/M1_0_SWE_TIME_SERIES_PROFILE.xml $BASE_URL/SWE-SingleStation-TimeSeriesProfile.xml

#fix line endings
find ./ -type f -exec dos2unix {} +
