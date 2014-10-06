# IOOS SOS Validator

## NOTE: For IOOS SOS compliance testing, see [ioos-sos-compliance-tests](https://github.com/ioos/ioos-sos-compliance-tests) 

This project is still useful for simple schema validation of SOS responses and templates. It is used in the build process of the 
[i52n-SOS](https://github.com/ioos/i52n-sos) project.  However, comprehensive tests ensuring IOOS SOS template compliance have not
been implemented in this project.

For more thorough IOOS SOS standard validation see the [ioos-sos-compliance-tests](https://github.com/ioos/ioos-sos-compliance-tests) project.

###Usage 

Validation tool for IOOS SOS responses. Use it:

 * As part of your Java build process (jUnit tests/suites)
 * On the command line, against a live SOS server
 * On the command line, against a directory of XML files
 * On the command line, against the IOOS SOS GitHub template repository

### Examples

Validate the [IOOS GitHub SOS milestone 1.0 templates](https://github.com/ioos/sos-guidelines/tree/master/template/milestone1.0):

    java -jar ioos-sos-validator.jar -gh 1.0
