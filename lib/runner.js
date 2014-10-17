module.exports.run = function (opts, cb) {
    var language = require('./config').officialLanguageName[opts.language];
    opts.language = language;
    // opts.solution is deprecated; this will go away
    opts.solution = opts.code;
    return require("./runners/" + language).run(opts,cb);
};
