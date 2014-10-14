var shovel = require('../shovel'),
	tmp=require('temporary');

module.exports.run = function run(opts, cb)
{
    shovel.start(opts, cb, {
        solutionOnly: function ()
        {
            var code = opts.code;
            if (opts.setup) code = opts.setup + '\n' + code;
            var file = new tmp.File();
            file.writeFileSync(code);
            return {name: 'csharp', 'args': [file.path]};
        }
    });
};

