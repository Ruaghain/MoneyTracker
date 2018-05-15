module.exports = function (grunt) {

    require("matchdep").filterDev("grunt-*").forEach(grunt.loadNpmTasks);

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        bower: {
            install: {
                options: {
                    install: true,
                    copy: true
                }
            }
        },
        karma: {
            options: {
                configFile: 'karma.conf.js'
            },
            unit: {
                background: true,
                singleRun: false
            }
        },
        copy: {
            main: {
                files: [
                    {
                        expand: true,
                        cwd: 'web/src/main/resources/static/',
                        src: '**/*.png',
                        dest: 'web/src/main/resources/static/images/',
                        flatten: true,
                        filter: 'isFile'
                    }
                ]
            }
        },
        wiredep: {
            target: {
                src: "web/src/main/resources/static/index.html",
                options: {
                    exclude: ["web/src/main/resources/static/vendors/angular/angular.js",
                        "web/src/main/resources/static/vendors/angular-base64/angular-base64.js"]
                }
            }
        },
        less: {
            development: {
                options: {
                    compress: true,
                    yuicompress: false,
                    optimization: 2,
                    cleancss: false,
                    paths: ["web/src/main/resources/static/components/**/*.less",
                        "web/src/main/resources/static/functionality/**/*.less"],
                    syncImport: false,
                    strictUnits: false,
                    strictMath: true,
                    strictImports: true,
                    ieCompat: false
                },
                files: {
                    "web/src/main/resources/static/css/<%= pkg.name %>.css": "web/src/main/resources/static/css/<%= pkg.name %>.less"
                }
            }
        },
        concat: {
            js: {
                options: {
                    stripBanners: true
                },
                src: ["web/src/main/resources/static/*.js",
                    "web/src/main/resources/static/components/**/*.js",
                    "web/src/main/resources/static/functionality/**/*.js",
                    "!**/*-test.js"],
                dest: "web/src/main/resources/static/js/<%= pkg.name %>.js"
            },
            less: {
                src: ["web/src/main/resources/static/css/moneyTracker-theme.less",
                    "web/src/main/resources/static/components/**/*.less",
                    "web/src/main/resources/static/functionality/**/*.less"],
                dest: 'web/src/main/resources/static/css/<%= pkg.name %>.less'
            }
        },
        uglify: {
            options: {
                // the banner is inserted at the top of the output
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n',
                mangle: true
            },
            dist: {
                files: {
                    'web/src/main/resources/static/dist/<%= pkg.name %>.min.js': ['<%= concat.js.dest %>']
                }
            }
        },
        jshint: {
            all: ['web/src/main/resources/static/*.js',
                'web/src/main/resources/static/components/**/*.js',
                'web/src/main/resources/static/functionality/**/*.js'],
            options: {
                // options here to override JSHint defaults
                globals: {
                    "angular": true
                }
            }
        },
        watch: {
            livereload: {
                options: {
                    atBegin: true,
                    livereload: true
                },
                files: ["web/src/main/resources/static/js/*.js",
                    "web/src/main/resources/static/MoneyTracker.css",
                    "web/src/main/resources/static/index.html"],
                tasks: []
            },
            scripts: {
                files: ["web/src/main/resources/static/*.js",
                    "web/src/main/resources/static/components/**/*.js",
                    "web/src/main/resources/static/functionality/**/*.js"
                ],
                tasks: ["jshint", "concat:js"]
            },
            less: {
                files: ["web/src/main/resources/static/components/**/*.less",
                    "web/src/main/resources/static/functionality/**/*.less",
                    "web/src/main/resources/static/vendors/angular-lookup-directive/*.less"],
                tasks: ["concat:less", "less"],
                options: {
                    nospawn: true
                }
            }
        },
        connect: {
            server: {
                options: {
                    hostname: 'localhost',
                    port: 9000,
                    base: 'web/src/main/resources/static/',
                    livereload: true,
                    open: true,
                    middleware: function (connect, options) {
                        if (!Array.isArray(options.base)) {
                            options.base = [options.base];
                        }

                        // Setup the proxy
                        var middlewares = [require('grunt-connect-proxy/lib/utils').proxyRequest];

                        // Serve static files.
                        options.base.forEach(function (base) {
                            middlewares.push(connect.static(base));
                        });

                        // Make directory browse-able.
                        var directory = options.directory || options.base[options.base.length - 1];
                        middlewares.push(connect.directory(directory));

                        return middlewares;
                    }
                },
                proxies: [
                    {
                        context: '/api',
                        host: 'localhost',
                        port: 8080
                    }
                ]
            }
        }
    });

    grunt.registerTask("server", ["configureProxies:server", "connect"]);
    grunt.registerTask("dev", ["copy", "concat", "less", "karma", "server", "watch"]);
    grunt.registerTask("prod", ["copy", "wiredep", "jshint", "concat", "uglify"]);
};