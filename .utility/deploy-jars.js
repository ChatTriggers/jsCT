if (process.env.TRAVIS_REPO_SLUG === "ChatTriggers/ct.js"
    && process.env.TRAVIS_PULL_REQUEST == "false"
    && (process.env.TRAVIS_BRANCH === "master" || process.env.TRAVIS_BRANCH === "travis")) {

    if (!process.env.TRAVIS_COMMIT_MESSAGE.includes("--release stable")
        && !process.env.TRAVIS_COMMIT_MESSAGE.includes("--release snapshot")) {
        console.log("Not deploying jars!");
        return;
    }

    var folderToDeploy = process.env.TRAVIS_COMMIT_MESSAGE.includes("--release stable") ? "stable" : "snapshot";

    console.log("Publishing jars...");

    var { exec } = require('child_process');

    exec("mkdir jars");
    exec("cp ../build/libs/* jars");
    console.log("Listing files...");


    var FtpClient = require('ftp-client');
    var client = new FtpClient({
        host: 'chattriggers.com',
        port: 21,
        user: process.env.FTP_USER,
        password: process.env.FTP_PASSWORD
    }, {
        logging: 'basic'
    });

    client.connect(function () {
        client.upload(['jars/*'], '/public_html/versions/' + folderToDeploy, {
            baseDir: 'jars',
            overwrite: 'all'
        }, function (result) {
            console.log(result);
            console.log("Published jars to production.");
        });
    });
}
