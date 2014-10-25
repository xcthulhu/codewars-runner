var expect = require('chai').expect,
    runner = require('../../lib/runner'),
    fs = require('fs');

describe( 'c# runner', function() {
    describe( '.run', function() {
        it('should handle basic code evaluation', function (done) {
            runner.run({language: 'csharp',
                        code:
                        'public class Hello1\n' +
                        '{\n' +
                        '    public static void Main()\n' +
                        '    {\n' +
                        '        System.Console.WriteLine("Hello, World!");\n' +
                        '    }\n' +
                        '}\n'
                       }, function (buffer)
                       {
                           console.log(buffer);
                           expect(buffer.stdout).to.equal('Hello, World!\n');
                           done();
                       });
        });

        it('should handle basic nunit tests', function (done)
           {
               runner.run({ language: 'csharp',
                            code: 'namespace Bank { using System; public class Account { private decimal balance; public void Deposit(decimal amount) { Console.WriteLine("slorgs"); balance += amount; } public void Withdraw(decimal amount) { balance -= amount; } public void TransferFunds(Account destination, decimal amount) { } public decimal Balance { get { return balance; } } } } ',
                            fixture: 'namespace Bank { using NUnit.Framework; [TestFixture] public class AccountTest { [Test] public void TransferFunds() { Account source = new Account(); source.Deposit(200m); Account destination = new Account(); destination.Deposit(150m); source.TransferFunds(destination, 100m); Assert.AreEqual(250m, destination.Balance); Assert.AreEqual(100m, source.Balance); } [Test] public void CheckFunds() { Account source = new Account(); source.Deposit(200m); Account destination = new Account(); destination.Deposit(150m); Assert.AreEqual(200m, source.Balance); } } } ' }, function (buffer)
                          {
                              console.log(buffer);
                              expect(buffer.stdout).to.contain("slorgs");
                              expect(buffer.stdout).to.contain("<PASSED::>");
                              expect(buffer.stdout).to.contain("<FAILED::>");
                              done();
                          });
           });
    });
});
