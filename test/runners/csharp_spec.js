var expect = require('chai').expect;
var runner = require('../../lib/runners/csharp');

describe( 'c# runner', function(){
    describe( '.run', function()
    {
        it('should handle basic code evaluation', function (done)
        {
            runner.run({language: 'csharp', solution: 'Console.WriteLine(1+1);'}, function (buffer)
            {
                expect(buffer.stdout).to.equal('2\n');
                done();
            });
        });

        it('should handle basic code evaluation from file', function (done)
        {
            runner.run({language: 'csharp', solutionFile: 'test/csharp/solution1.cs'}, function (buffer)
            {
                console.log(buffer.stdout);
                expect(buffer.stdout).to.equal('Hello World\n');
                done();
            });
        });

        it('should handle basic nunit tests', function (done)
        {
            require('../../lib/opts').process({language: 'csharp', solutionFile: 'test/csharp/Account.cs', fixtureFile: 'test/csharp/AccountTest.cs'}, function(opts)
            {
                runner.run(opts, function (buffer)
                {
                    console.log(buffer.stdout);
                    assert(buffer.stdout.indexOf('<FAILED::>') != -1);
                    assert(buffer.stdout.indexOf('<PASSED::>') != -1);
                    done();
                });
            });
        });
    });
});
