module.exports.run = function (opts) {
    var language = require('./config').officialLanguageName[opts.language];
    opts.language = language;
    return require("./runners/" + language + ".js").run(opts);
};
