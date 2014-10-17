var expect = require('chai').expect;
var runner = require('../runner');

describe( 'c# runner', function(){
    describe( '.run', function() {
        it('should handle basic code evaluation', function (done) {
            runner.run({language: 'csharp', code: 'Console.WriteLine(1+1);'}, function (buffer) {
                expect(buffer.stdout).to.equal('2\n');
                done();
            });
        });

        it('hello world', function (done) {
            runner.run({language: 'csharp',
                        code: 'Console.WriteLine("Hello")'},
                       function (buffer){
                           expect(buffer.stdout).to.contain('Hello');
                           done();
                       });
        });
    });
});
