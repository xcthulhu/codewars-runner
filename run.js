var config = require('./lib/config' ),
    opts = require("nomnom")
        .options({
            solution: {
              abbr: 'c',
              help: "solution code to run"
            },
            fixture: {
                abbr: 'f',
                help: 'Test fixture code to test with'
            },
            setup: {
                abbr: 's',
                help: 'Setup code to be used for executing the code'
            },
            language: {
                abbr: 'l',
                help: 'The language to execute the code in'
            },
            testFramework: {
                abbr: 't',
                full: 'test-framework',
                help: 'The language specific framework to run in'
            },
            timeout: {
                help: 'The timeout to be used for running the code. If not specified a language specific default will be used'
            },
            format: {
                help: 'The output format that will be returned. Options are "default" and "json"',
                default: 'default',
                choices: ['default', 'json'],
                abbr: 'fmt'
            },
            debug: {
                abbr: 'd',
                help: 'Print debugging info',
                flag: true
            },
            version: {
                abbr: 'v',
                flag: true,
                help: 'Print version and exit',
                callback: function () {
                    return config.version;
                }
            }
        })
        .help('This utility will run code in a specified language, using the specified testing suite.')
        .parse();

require('./lib/opts').run(opts);
