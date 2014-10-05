# IOOS SOS Validator

## NOTE: This project has been superseded
This project is still useful for simple schema validation of SOS responses and templates.

For more thorough IOOS standard validation see the [ioos-sos-compliance-tests](https://github.com/ioos/ioos-sos-compliance-tests) project.

###Usage 

Validation tool for IOOS SOS responses. Use it:

 * As part of your Java build process (jUnit tests/suites)
 * On the command line, against a live SOS server
 * On the command line, against a directory of XML files
 * On the command line, against the IOOS SOS GitHub template repository

### Examples

Validate the [IOOS GitHub SOS milestone 1.0 templates](https://github.com/ioos/sos-guidelines/tree/master/template/milestone1.0):

    java -jar ioos-sos-validator.jar -gh 1.0
