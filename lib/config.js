module.exports = {
    version: '1.0.2',
    testFramework: {
        defaults: {
            javascript: 'cw-2',
            coffeescript: 'cw-2',
            ruby: 'cw-2',
            python: 'cw-2'
        }
    },
    images: {
        ruby: ['ruby'],
        node: ['javascript', 'coffeescript', 'typescript'],
        python: ['python'],
        dotnet: ['csharp', 'fsharp'],
        jvm: ['java', 'groovy', 'clojure'],
        func: ['haskell', 'ocaml', 'racket', 'lisp'],
        systems: ['c', 'cpp', 'nasm', 'gas', 'bash', 'arm'],
        alt: ['r', 'rust','erlang', 'elixir', 'go', 'julia', 'lua', 'php', 'perl']
    },
    timeouts: {
        default: 6000,
        clojure: 10000,
        java: 10000
    },
    moduleRegExs: {
        haskell: /module\s+([A-Z]([a-z|A-Z|0-9]|\.[A-Z])*)\W/,
        clojure: /\(ns\s+([A-Z|a-z]([a-z|A-Z|0-9|-]|\.[A-Z|a-z])*)\W/,
        julia: /module\s+([a-z|A-Z][a-z|A-Z|0-9]*)\W/,
        erlang: /-module\(([a-z|A-Z][a-z|A-Z|0-9|_]*)\)/
    },
    fileExtensions: {
        haskell: 'hs',
        clojure: 'clj',
        julia: 'jl',
        erlang: 'erl'
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
            requireCw2: [//"from solution import *", - taking out for now, not working well with preloaded code since we dont want the preloaded code to be easily read.
	                 "reload(__import__('sys')).path.append('./frameworks/python')",
	                 "test = Test = reload(__import__('cw-2'))"].join("\n"),
            requireUnittest: "import sys\nsys.path.append('./frameworks/python/')\nimport unittest\n",
            defaultTestSuite: "_testsuite = _testsuite or unittest.TestLoader().loadTestsFromTestCase(Test)",
            runUnittest: "import unittestwrapper\n_testresult = unittestwrapper.CwTestResult()\n_testsuite.run(_testresult)"
        },
        clojure: {
            runTests: ["(require '[clojure.test.codewars])",
                       "(clojure.test.codewars/run-tests)"].join('\n')
        }
    }
};
