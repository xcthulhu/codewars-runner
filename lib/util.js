var fs = require('fs'),
    path = require('path'),
    config = require('./config'),
    child_process = require('child_process'),
    clc = require('cli-color');

module.exports.exec = function (command, opts, cb) {
    function handleOutput (error, stdout, stderr) {
        if (stdout) console.log(stdout);
        if (stderr) throw new Error(stderr);
        if (error !== null) throw error;
        cb();
    }
    if (opts) child_process.exec(command, opts, handleOutput);
    else child_process.exec(command, handleOutput);
};

// mkdir -p
function mkdirParentSync(dirPath, mode) {
    var dirs = dirPath.split("/"), partialPath;
    for (var idx = (dirPath[0] == '/' ? 1 : 0); idx < dirs.length; idx++) {
        partialPath = dirs.slice(0, idx + 1).join("/");
        if (!fs.existsSync(partialPath))
            fs.mkdirSync(partialPath, mode);
    }
}

module.exports.mkdirParentSync = mkdirParentSync;

// Infer the name of a file from a module or namespace declaration in the code
function codeFileName(language, code, defaultFileName) {
    if (config.moduleRegExs[language]) {
        var match = config.moduleRegExs[language].exec(code);
        return match !== null ?
            match[1].replace(/\./g, '/').replace(/-/g, '_') + '.' + config.fileExtensions[language] :
            defaultFileName;
    }
    else {
        return defaultFileName || language;
    }
}

module.exports.codeWriteSync = function (language, code, codeDir, defaultFileName, overwrite) {
    var fileName = codeFileName(language, code, defaultFileName);
    if (!(typeof fileName == 'string' || fileName instanceof String))
        throw new Error(["Could not determine valid name from code:\n\n", code].join(""));
    if (code == "")
        throw new Error(["Code cannot be empty!"]);
    fileName = path.join(codeDir, fileName);
    if (fs.existsSync(fileName))
        if (!overwrite) {
            throw new Error(["Could not write code to file ", fileName,
                " because file already exists:\n\n", code].join(""));
        }
    mkdirParentSync(path.dirname(fileName));
    fs.writeFileSync(fileName, code);
    return fileName;
};
