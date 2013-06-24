#!/bin/bash
BASE_URL=https://ioostech.googlecode.com/svn/trunk/templates/Milestone1.0
DIR=`dirname $0`
wget -O $DIR/M1_0_CAPABILITIES.xml $BASE_URL/SOS-GetCapabilities.xml
wget -O $DIR/M1_0_SENSOR_ML_NETWORK.xml $BASE_URL/SML-DescribeSensor-Network.xml
wget -O $DIR/M1_0_SENSOR_ML_STATION $BASE_URL/SML-DescribeSensor-Station.xml
