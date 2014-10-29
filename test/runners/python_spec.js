var expect = require('chai').expect;
var runner = require('../runner');

describe('python runner', function () {

    describe('.run', function () {
        it('should handle basic code evaluation', function (done) {
            runner.run({language: 'python', code: 'import sys; sys.stdout.write("42")'}, function (buffer) {
                console.log(buffer);
                expect(buffer.stdout).to.equal('42');
                done();
            });
        });
        it('stderr', function (done) {
            runner.run({language: 'python', code: 'import sys; sys.stderr.write("Error!  Codewars cannot and will not accept any more Fibonacci kata.")'}, function (buffer) {
                console.log(buffer);
                expect(buffer.stderr).to.equal("Error!  Codewars cannot and will not accept any more Fibonacci kata.");
                done();
            });
        });
        it('stderr', function (done) {
            runner.run({language: 'python', code: 'import sys; sys.stderr.write("florp"); sys.stdout.write("foop")'}, function (buffer) {
                console.log(buffer);
                expect(buffer.stderr).to.equal("florp");
                expect(buffer.stdout).to.equal("foop");
                done();
            });
        });
    });
    describe('cw-2', function () {
        it('should handle a basic assertion', function (done) {
            runner.run({
                    language: 'python',
                    code: 'a = 1',
                    fixture: 'test.expect(a == 1)',
                    testFramework: 'cw-2'
                },
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.equal('<PASSED::>Test Passed\n');
                    done();
                });
        });
        it('should handle a basic assert_equals', function (done) {
            runner.run({
                    language: 'python',
                    code: 'a = 1',
                    fixture: 'test.assert_equals(a, 1)',
                    testFramework: 'cw-2'
                },
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.equal('<PASSED::>Test Passed\n');
                    done();
                });
        });
        it('should handle a basic setup', function (done) {
            runner.run({
                    language: 'python',
                    code: 'a = 1',
                    setup: 'b = 2',
                    fixture: 'test.assert_equals(b, 2)',
                    testFramework: 'cw-2'
                },
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.equal('<PASSED::>Test Passed\n');
                    done();
                });
        });
        it('should handle a failed assertion', function (done) {
            runner.run({
                    language: 'python',
                    code: 'a = 1',
                    fixture: 'test.expect(a == 2)',
                    testFramework: 'cw-2'
                },
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.equal('<FAILED::>Value is not what was expected\n');
                    done();
                });
        });

        it('should handle a failed assertion', function (done) {
            runner.run({language: 'python',
                    code: 'a.fail()',
                    testFramework: 'cw-2'},
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stderr).to.not.contain('File ');
                    expect(buffer.stderr).to.not.contain(', line ');
                    expect(buffer.stderr).to.not.contain('most recent call last');
                    done();
                });
        });
    });
    describe('unittest', function () {
        it('should handle a basic assertion', function (done) {
            runner.run({language: 'python',
                    code: 'a = 1',
                    fixture: [
                        'class Test(unittest.TestCase):',
                        '  def test_assert(self):',
                        '    self.assertEqual(a, 1)'
                    ].join('\n'),
                    testFramework: 'unittest'},
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.equal('<PASSED::>Test Passed\n');
                    done();
                });
        });
        it('should handle a failed assetion', function (done) {
            runner.run({language: 'python',
                    code: 'a = 1',
                    fixture: [
                        'class Test(unittest.TestCase):',
                        '  def test_assert(self):',
                        '    self.assertEqual(a, 2, "test failed")'
                    ].join('\n'),
                    testFramework: 'unittest'},
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.contain('<FAILED::>');
                    expect(buffer.stdout).to.contain('test failed');
                    done();
                });
        });
        it('should handle a failed assetion', function (done) {
            runner.run({language: 'python',
                    code: 'a = 1',
                    fixture: [
                        'class Test(unittest.TestCase):',
                        '  def test_assert(self):',
                        '    raise Exception("exceptions are my favorite, I always throw them")'
                    ].join('\n'),
                    testFramework: 'unittest'},
                function (buffer) {
                    console.log(buffer);
                    expect(buffer.stdout).to.equal('<ERROR::>Unhandled Exception: exceptions are my favorite, I always throw them\n');
                    done();
                });
        });
    });
});
