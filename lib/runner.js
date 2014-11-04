module.exports.run = function (opts, cb) {
    // opts.solution is deprecated; this will go away
    opts.solution = opts.code;
    return require("./runners/" + opts.language).run(opts,cb);
};
