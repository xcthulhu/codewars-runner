var shovel = require('../shovel'),
    util = require('../util'),
    temp = require('temp').track(),
    path = require('path'),
    nunitAssemblies = ['nunit.core.dll', 'nunit.core.interfaces.dll', 'nunit.util.dll', 'nunit.framework.dll'].join(','),
    nunitPath = path.resolve(__dirname, '..', '..', 'frameworks', 'csharp', 'nunit');

module.exports.run = function run(opts, cb) {
    shovel.start(opts, cb,
        {
            solutionOnly: function () {
                return function (run) {
		    var dir = temp.mkdirSync('csharp'),
			codeFile = util.codeWriteSync('csharp', opts.solution, dir, 'code.cs'),
                        exe = path.join(dir, 'solution.exe'),
                        args = ['mcs', '-out:' + exe, codeFile];
                    if (opts.setup) args.push(util.codeWriteSync('csharp', opts.solution, dir, 'setup.cs'));
                    util.exec(args.join(' '), function () { run({'name': 'mono', 'args': [exe]}); });
                };
            },
            fullProject: function () {
                return function (run) {
		    var dir = temp.mkdirSync('csharp'),
                        fixtureFile = util.codeWriteSync('csharp', opts.fixture, dir, 'fixture.cs'), 
			codeFile = util.codeWriteSync('csharp', opts.solution, dir, 'code.cs'),
                        testDLL = path.join(dir, 'test.dll'),
                        args = [
                            'mcs',
                                '-out:' + testDLL,
                                '-lib:' + nunitPath,
                                '-r:' + nunitAssemblies,
                            '-target:library',
                            codeFile,
                            fixtureFile
                        ];
                    if (opts.setup) args.push(util.codeWriteSync('csharp', opts.solution, dir, 'setup.cs'));

                    util.exec(args.join(' '),
                        function () {
                            run({
                                name: "mono",
                                args: [ 
                                    path.join(nunitPath, 'nunit-console.exe'),
                                    '-nologo',
                                    '-noresult',
                                    testDLL
                                ],
                                options: {env: process.env}
                            });
                        });
                };
            }
        });
};

