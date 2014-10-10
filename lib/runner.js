module.exports.run = function (opts, cb) {
    var language = require('./config').officialLanguageName[opts.language];
    opts.language = language;
    return require("./runners/" + language).run(opts,cb);
};
