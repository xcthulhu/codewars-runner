var expect = require('chai').expect;
var runner = require('../runner');


describe('rust runner', function () {
    describe('.run', function () {
        it('should handle basic code evaluation', function (done) {
            runner.run({language: 'rust', code: 'fn main() { println!("Hello Rust!"); }'},
                function (buffer) {
                    expect(buffer.stdout).to.equal('Hello Rust!\n');
                    done();
                });
        });
    });
});
