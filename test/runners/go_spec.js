var expect = require('chai').expect;
var runner = require('../runner');

describe( 'go runner', function(){
    describe( '.run', function(){
        it( 'should handle basic code evaluation', function(done){
            var code = ['package main',
                            'import "fmt"',
                            'func main() {',
                            '    fmt.Println("42")',
                            '}',
                            ''].join('\n');

            runner.run({language: 'go', code: code}, function(buffer) {
                expect(buffer.stdout).to.equal('42\n');
                done();
            });
        });
    });
});
