var shovel = require('../shovel'),
    config = require('../config'),
    codeWriteSync = require('../util').codeWriteSync,
    fs = require('fs'),
    path = require('path'),
    temp = require('temp');

function moduleName(fileName) {
    return path.basename(fileName).replace(/\.[^/.]+$/, "");
}

function erlangCompileCommand(fileName, erlangCodeDir) {
    return [
        'compile:file("',
        fileName,
        '", {outdir,"', erlangCodeDir ,'"}),'
    ].join('');
}

function compileFileSync(code,erlangCodeDir) {
    return erlangCompileCommand( codeWriteSync('erlang', code, erlangCodeDir), erlangCodeDir);
}

module.exports.run = function run(opts, cb) {
    temp.track();
    var erlangCodeDir = temp.mkdirSync('erlang');
    shovel.start(opts, cb, {
        solutionOnly: function () {
            var setup = opts.setup ? compileFileSync(opts.setup, erlangCodeDir) : '';
            return {
                name: 'erl',
                args: ['-pz', erlangCodeDir, '-noshell', '-eval', [setup, opts.solution].join('')],
                options: { env: {
                    HOME: process.env['HOME'],
                    ERL_CRASH_DUMP: "/dev/null"
                }}
            };
        }
    });
};
