module.exports = {
    version: '1.0.5',
    testFramework: {
        defaults: {
            javascript: 'cw-2',
            coffeescript: 'cw-2',
            ruby: 'cw-2',
            python: 'cw-2'
        }
    },
    timeouts: {
        default: 6000,
        clojure: 10000,
        java: 10000
    },
    moduleRegExs: {
        haskell: /module\s+([A-Z]([a-z|A-Z|0-9]|\.[A-Z])*)\W/,
        julia: /module\s+([a-z|A-Z][a-z|A-Z|0-9]*)\W/,
        erlang: /-module\(([a-z|A-Z][a-z|A-Z|0-9|_]*)\)/,
        scala: /(?:object|class)\s+([A-Z][a-z|A-Z|0-9|_]*)/
    },
    fileExtensions: {
        haskell: 'hs',
        julia: 'jl',
        erlang: 'erl',
        scala: 'scala'
    },
    snippets: {
        javascript: {
            requireCw2: "require('./frameworks/javascript/cw-2')\n",
            start: "Test.handleError(function(){\n",
            inlineTestFixture: {
                start: "\n(function() { var Test = global.Test, describe = global.describe, it = global.it, before = global.before, after = global.after;",
                end: '\n})();'
            },
            end: "});"
        },
        python: {
            addPythonFramework: [
                "try: from imp import reload",
                "except: pass",
                "reload(__import__('sys')).path.append('./frameworks/python')"
            ].join('\n'),
            requireCw2: "test = Test = reload(__import__('cw-2'))",
            requireUnittest: "unittest = reload(__import__('unittest'))",
            runUnittest: [
                "unittest",
                ".TestLoader()",
                ".loadTestsFromTestCase(Test)",
                ".run(reload(__import__('unittestwrapper')).CwTestResult())"
            ].join('')
        }
    }
};

module.exports.snippets.python3 = module.exports.snippets.python;
