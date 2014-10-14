var shovel = require('../shovel'),
	tmp=require('temporary');

module.exports.run = function run(opts, cb)
{
    shovel.start(opts, cb, {
        solutionOnly: function ()
        {
            var code = opts.solution;
            if (opts.setup)
            {
                code = opts.setup + '\n' + code;
            }
            // Always use temp file even if code can be passed by arg.
            var file = new tmp.File();
            console.log(code);
            file.writeFileSync(code);
            return {name: 'csharp', 'args': [file.path]};
        }
    });
};

