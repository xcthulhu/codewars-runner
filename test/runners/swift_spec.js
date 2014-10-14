var expect = require('chai').expect;
var runner = require('../runner');


describe( 'swift runner', function(){
    describe( '.run', function(){
        it( 'should handle basic code evaluation', function(done){
            runner.run({language: 'swift', code: "println(\"You've baked a really lovely cake, but then you've used dog shit for frosting. - Steve Jobs (commenting on an employee's program)\")"}, function(buffer) {
		console.log(buffer);
                expect(buffer.stdout).to.equal("You've baked a really lovely cake, but then you've used dog shit for frosting. - Steve Jobs (commenting on an employee's program)\n");
                done();
            });
        });
    });
});
