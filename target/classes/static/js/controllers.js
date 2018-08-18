'use strict';

//----employe controller-----//
app.controller("employeController",function ($scope,$http,$mdDialog) {
    $scope.pageEmployes = [];
    $scope.listeEmployes= [];
    $scope.services = [];
    $scope.employe = {};
    $scope.pages = [];
    $scope.pageCourante = 0;
    $scope.size = 5;
    $scope.motcle = "";

    //lister les employes par nom
    $scope.listerallEmployes = function () {
        $scope.chercher();
    };

    //fonction pour faire la recherche par nom
    $scope.chercher = function(){
        $http.get("http://localhost:8087/listeEmployesParnom?page="+$scope.pageCourante+"&size="+$scope.size+"&motcle="+$scope.motcle+"")
            .then(function success(response) {
                    $scope.pageEmployes = response.data;
                    $scope.pages = new Array(response.data.totalPages);
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );

    };
    //fonction pour faire la recherche par nom


    //fonction pour ajouter un employé
    $scope.ajouterEmpolye = function(){
        $http.post("http://localhost:8087/addEmploye",$scope.employe)
            .then(function success(response) {
                    $scope.employe = response.data;
                    console.log("ajout ok");
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );

    };

    $scope.status = '';
    $scope.customFullscreen = false;
    $scope.editEmploye = function (ev,index) {
        $http.get("http://localhost:8087/employe/get/"+index)
            .then(function success(response) {
                    var parentEl = angular.element(document.body);
                    $mdDialog.show({
                        controller: dialogEmployeController,
                        controllerAs:'crtl',
                        templateUrl: 'views/modifEmploye.html',
                        parent: parentEl,
                        targetEvent: ev,
                        clickOutsideToClose:false,
                        fullscreen: $scope.customFullscreen,// Only for -xs, -sm breakpoints.
                        locals:{
                            employee : response.data,
                            services : $scope.services
                        }
                    })
                        .then(function(answer) {
                            $scope.status = 'You said the information was "' + answer + '".';
                        }, function() {
                            $scope.status = 'You cancelled the dialog.';
                        });
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    function dialogEmployeController($scope, $mdDialog,employee,services) {
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.answer = function(answer) {
            $mdDialog.hide(answer);
        };
        $scope.employe = employee;
        $scope.servicees = services;
        console.log($scope.employe);

        //fonction pour modifier un employé
        $scope.modifierEmploye = function(){
            $http.put("http://localhost:8087/employe/update",$scope.employe)
                .then(function success(response) {
                        $scope.employe = response.data;
                        console.log("modification ok");
                        $scope.cancel();
                    },
                    function error(response) {
                        if (response.error instanceof Error) {
                            console.log("client-side");
                        } else {
                            console.log("server-side");
                        }
                    }
                );

        };
    }

        //fonction pour supprimer un employé
    $scope.supprimerEmpolye = function(index){
        $http.delete("http://localhost:8087/employe/delete/"+index)
            .then(function success() {
                    console.log("suppression ok");
                    $scope.listerallEmployes();
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );

    };


    //fonction pour lister tous les services
    $scope.listeServices = function(){
        $http.get("http://localhost:8087/listServices")
            .then(function success(response) {
                    $scope.services = response.data;
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //lister toutes les employes au chargement
    $scope.listerallEmployes();

    //lister toutes les services au chargement
    $scope.listeServices();

    $scope.gotopage = function (index) {
        $scope.pageCourante = index;
        $scope.listerallEmployes();
    };
    $scope.resize = function (index) {
        $scope.size = index;
        $scope.pageCourante = 0;
        $scope.listerallEmployes();
    }

}).filter('highlight', function($sce) {
    return function(text, phrase) {
        if (phrase) text = text.replace(new RegExp('('+phrase+')', 'gi'),
            '<span class="highlighted" style="color: #6b0392; ' +
            'font-size: 15px; font-weight: bold; background-color: #c1e2b3">$1</span>');

        return $sce.trustAsHtml(text);
    }
});


//----user controller-----//
app.controller("userController",function ($scope,$http) {
    $scope.pageUsers = [];
    $scope.users = [];
    $scope.pages = [];
    $scope.services = [];
    $scope.pageCourante = 0;
    $scope.size = 5;
    $scope.motcle = "";

    $scope.user = {};

    //lister tous les utilisateurs par page
    $scope.listeallUsers = function () {
        $http.get("http://localhost:8087/listeUsers?motcle=" + $scope.motcle + "&page=" + $scope.pageCourante + "&size=" + $scope.size)
            .then(function success(response) {
                    $scope.pageUsers= response.data;
                    $scope.pages = new Array(response.data.totalPages);
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //lister tous les utilisateurs par page
    $scope.listeUsers = function () {
        $http.get("http://localhost:8087/listUsers")
            .then(function success(response) {
                    $scope.users= response.data;

                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //fonction pour ajouter les utilisateurs
    $scope.ajouterUser = function(){
        $http.post("http://localhost:8087/addUser",$scope.user)
            .then(function success(response) {
                    console.log("ajout ok");
                    $scope.users= response.data;
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //fonction pour supprimer les utilisateurs
    $scope.supprimerUser = function(index){
        $http.delete("http://localhost:8087/user/delete/"+index)
            .then(function success() {
                    console.log("delete ok");
                    $scope.listeallUsers();
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };
    //cherger les utilisateurs au demarrage
    $scope.listeallUsers();

    $scope.chercher = function(){
        $scope.pageCourante = 0;
        $scope.listeallUsers();
    };
    $scope.gotopage = function (index) {
        $scope.pageCourante = index;
        $scope.listeallUsers();
    };
    $scope.resize = function (index) {
        $scope.size = index;
        $scope.pageCourante = 0;
        $scope.listeallUsers();
    };

}).filter('highlight', function($sce) {
    return function(text, phrase) {
        if (phrase) text = text.replace(new RegExp('('+phrase+')', 'gi'),
            '<span class="highlighted" style="color: #6b0392; ' +
            'font-size: 15px; font-weight: bold; background-color: #c1e2b3">$1</span>');

        return $sce.trustAsHtml(text);
    }
});


//----service controller-----//
app.controller("serviceController",function ($scope,$http,$mdDialog) {
    $scope.pageServices = [];
    $scope.pages = [];
    $scope.services = [];
    $scope.pageCourante = 0;
    $scope.size = 5;
    $scope.motcle = "";

    $scope.service = {};

    //lister tous les services par page
    $scope.listeallServices = function () {
        $http.get("http://localhost:8087/listeServices?motcle=" + $scope.motcle + "&page=" + $scope.pageCourante + "&size=" + $scope.size)
            .then(function success(response) {
                    $scope.pageServices = response.data;
                    $scope.pages = new Array(response.data.totalPages);
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //lister tous les services par page
    $scope.listeServices = function () {
        $http.get("http://localhost:8087/listServices")
            .then(function success(response) {
                    $scope.services= response.data;

                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };


    //fonction pour ajouter un service
    $scope.ajouterService = function(){
        $http.post("http://localhost:8087/addService",$scope.service)
            .then(function success(response) {
                    $scope.service = response.data;
                    console.log("ajout ok");
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    $scope.status = '';
    $scope.customFullscreen = false;
    $scope.editService = function (ev,index) {
        $http.get("http://localhost:8087/service/get/"+index)
            .then(function success(response) {
                    var parentEl = angular.element(document.body);
                    $mdDialog.show({
                        controller: dialogServiceController,
                        controllerAs:'crtl',
                        templateUrl: 'views/modifService.html',
                        parent: parentEl,
                        targetEvent: ev,
                        clickOutsideToClose:false,
                        fullscreen: $scope.customFullscreen,// Only for -xs, -sm breakpoints.
                        locals:{
                            service : response.data
                        }
                    })
                        .then(function(answer) {
                            $scope.status = 'You said the information was "' + answer + '".';
                        }, function() {
                            $scope.status = 'You cancelled the dialog.';
                        });
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    function dialogServiceController($scope, $mdDialog,service) {
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.answer = function(answer) {
            $mdDialog.hide(answer);
        };
        $scope.service = service;
        console.log($scope.service);

        //fonction pour modifier un employé
        $scope.modifierService= function(){
            $http.put("http://localhost:8087/service/update",$scope.service)
                .then(function success(response) {
                        $scope.service = response.data;
                        console.log("modification ok");
                        $scope.cancel();
                    },
                    function error(response) {
                        if (response.error instanceof Error) {
                            console.log("client-side");
                        } else {
                            console.log("server-side");
                        }
                    }
                );

        };
    }

    //fonction pour supprimer un service
    $scope.supprimerService = function(index){
        $http.delete("http://localhost:8087/service/delete/"+index)
            .then(function success() {
                    console.log("delete ok");
                    $scope.listeallServices();
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };
    //cherger les entreprises au démarrage
    $scope.listeallServices();

    $scope.chercher = function(){
        $scope.pageCourante = 0;
        $scope.listeallServices();
    };
    $scope.gotopage = function (index) {
        $scope.pageCourante = index;
        $scope.listeallServices();
    };
    $scope.resize = function (index) {
        $scope.size = index;
        $scope.pageCourante = 0;
        $scope.listeallServices();
    };

}).filter('highlight', function($sce) {
    return function(text, phrase) {
        if (phrase) text = text.replace(new RegExp('('+phrase+')', 'gi'),
            '<span class="highlighted" style="color: #6b0392; ' +
            'font-size: 15px; font-weight: bold; background-color: #c1e2b3">$1</span>');

        return $sce.trustAsHtml(text);
    }
});



//----role controller-----//
app.controller("roleController",function ($scope,$http) {
    $scope.pageRoles = [];
    $scope.pages = [];
    $scope.pageCourante = 0;
    $scope.size = 5;
    $scope.motcle = "";

    $scope.role= {};

    //lister tous les roles par page
    $scope.listeallRoles = function () {
        $http.get("http://localhost:8087/listeRoles?motcle=" + $scope.motcle + "&page=" + $scope.pageCourante + "&size=" + $scope.size)
            .then(function success(response) {
                    $scope.pageRoles = response.data;
                    $scope.pages = new Array(response.data.totalPages);
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //fonction pour ajouter un role
    $scope.ajouterRole = function(){
        $http.post("http://localhost:8087/addRole",$scope.role)
            .then(function success(response) {
                    console.log("ajout ok");
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //fonction pour supprimer un role
    $scope.supprimerRole = function(index){
        $http.delete("http://localhost:8087/role/delete/"+index)
            .then(function success(response) {
                    console.log("delete ok");
                    $scope.listeallRoles();
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };
    //cherger les roles au démarrage
    $scope.listeallRoles();

    $scope.chercher = function(){
        $scope.pageCourante = 0;
        $scope.listeallRoles();
    };
    $scope.gotopage = function (index) {
        $scope.pageCourante = index;
        $scope.listeallRoles();
    };
    $scope.resize = function (index) {
        $scope.size = index;
        $scope.pageCourante = 0;
        $scope.listeallRoles();
    };

}).filter('highlight', function($sce) {
    return function(text, phrase) {
        if (phrase) text = text.replace(new RegExp('('+phrase+')', 'gi'),
            '<span class="highlighted" style="color: #6b0392; ' +
            'font-size: 15px; font-weight: bold; background-color: #c1e2b3">$1</span>');

        return $sce.trustAsHtml(text);
    }
});


//----userRole controller-----//
app.controller("userRoleController",function ($scope,$http) {
    //liste a recuperer
    $scope.ListRoles = [];
    $scope.ListUsers = [];

    //liste d'users par page
    $scope.pageUsers = [];

    //element important
    $scope.user_id = "";
    $scope.role_id = "";

    //
    $scope.pages = [];
    $scope.pageCourante = 0;
    $scope.size = 5;
    $scope.motcle = "";

    //lister tous les roles par page
    $scope.listeallUsers = function () {
        $http.get("http://localhost:8087/listeUsers?motcle=" + $scope.motcle + "&page=" + $scope.pageCourante + "&size=" + $scope.size)
            .then(function success(response) {
                    $scope.pageUsers = response.data;
                    $scope.pages = new Array(response.data.totalPages);
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //lister tous les roles
    $scope.listeRoles = function () {
        $http.get("http://localhost:8087/listRoles")
            .then(function success(response) {
                    $scope.listRoles = response.data;
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //lister tous les users
    $scope.listeUsers = function () {
        $http.get("http://localhost:8087/listUsers")
            .then(function success(response) {
                    $scope.listUsers = response.data;
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };

    //fonction pour ajouter un role à un user
    $scope.ajouterUserRole = function(){
        $http.get("http://localhost:8087/addRoleToUser?user_id="+$scope.user_id+"&role_id="+$scope.role_id)
            .then(function success(response) {
                    console.log("ok");
                    console.log(response.data);
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };


    //fonction pour supprimer un role à l'user
    $scope.supprimerUserRole = function(u,r){
        $http.get("http://localhost:8087/suppRoleToUser?user_id="+u+"&role_id="+r)
            .then(function success(response) {
                    console.log("ok");
                    console.log(response.data);
                    $scope.listeallUsers();
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                }
            );
    };


    //charger les roles au démarrage
    $scope.listeRoles();

    //charger les roles par pages au démarrage
    $scope.listeallUsers();

    //charger les users au démarrage
    $scope.listeUsers();

    $scope.chercher = function(){
        $scope.pageCourante = 0;
        $scope.listeallUsers();
    };
    $scope.gotopage = function (index) {
        $scope.pageCourante = index;
        $scope.listeallUsers();
    };
    $scope.resize = function (index) {
        $scope.size = index;
        $scope.pageCourante = 0;
        $scope.listeallUsers();
    };

}).filter('highlight', function($sce) {
    return function(text, phrase) {
        if (phrase) text = text.replace(new RegExp('('+phrase+')', 'gi'),
            '<span class="highlighted" style="color: #6b0392; ' +
            'font-size: 15px; font-weight: bold; background-color: #c1e2b3">$1</span>');

        return $sce.trustAsHtml(text);
    }
});


//----chartServiceBudget controller-----//
app.controller('chartBudgetParServiceController', function($scope,$http) {
    //initialisation du tableau qui accueillera la liste des servcices au depart
    $scope.servicesChart = [];

    //function permettra de charger notre charte
    $scope.chartBudgetParService = function (){
        $http.get("http://localhost:8087/listServices")
            .then(function success(response) {
                    $scope.servicesChart =  response.data;
                    console.log($scope.servicesChart);

                    //initialisation du tableau qui recevra la liste des noms et des budgets de service
                    var servicesAll = [];
                    var i;
                    for (i=0;i< $scope.servicesChart.length;i++){
                        servicesAll.push({label:$scope.servicesChart[i]["nom"],y:$scope.servicesChart[i]["budget"]});
                    }
                    console.log(servicesAll);

                    $scope.chart = new CanvasJS.Chart("statServices", {
                        animationEnabled: true,
                        theme: "theme1", // "light1", "light2", "dark1", "dark2"
                        title: {
                            text: "Statistique sur le budget des services"
                        },
                        axisY: {
                            title: "Budget par services(Frs)",
                            suffix: " frs",
                            includeZero: false,
                            labelFontSize:13
                        },
                        axisX: {
                            title: "Liste des services",
                            labelFontSize:13
                        },
                        data: [{
                            type: "column",
                            yValueFormatString: "#,##0.0#\" frs\"",
                            dataPoints: servicesAll
                        }]
                    });
                    $scope.chart.render();
                    $scope.changeChartType = function(chartType) {
                        $scope.chart.options.data[0].type = chartType;
                        $scope.chart.render(); //re-render the chart to display the new layout
                    };
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                });
    };

    $scope.chartBudgetParService();

});

//----chartEmployeService controller-----//
app.controller('chartEmployeParServiceController', function($scope,$http) {
    //initialisation du tableau qui accueillera la liste des servcices au depart
    $scope.servicesChart = [];

    //function permettra de charger notre charte
    $scope.chartEmployeParService = function (){
        $http.get("http://localhost:8087/listServices")
            .then(function success(response) {
                    $scope.servicesChart =  response.data;
                    console.log($scope.servicesChart);

                    //initialisation du tableau qui recevra la liste des noms et le nombre d'employés des services
                    var servicesAll = [];
                    var i;
                    for (i=0;i< $scope.servicesChart.length;i++){
                        servicesAll.push({label:$scope.servicesChart[i]["nom"],y:$scope.servicesChart[i]["employes"].length});
                    }
                    console.log(servicesAll);

                    $scope.chart = new CanvasJS.Chart("stat-employes-services", {
                        animationEnabled: true,
                        theme: "theme2", // "light1", "light2", "dark1", "dark2"
                        title: {
                            text: "Statistique sur le total d'employés par service"
                        },
                        axisY: {
                            title: "Employés par services(pers)",
                            suffix: " pers",
                            includeZero: false,
                            labelFontSize:13
                        },
                        axisX: {
                            title: "Liste des services",
                            labelFontSize:13
                        },
                        data: [{
                            type: "column",
                            yValueFormatString: "#,##0#\" personne(s)\"",
                            dataPoints: servicesAll
                        }]
                    });
                    $scope.chart.render();
                    $scope.changeChartType = function(chartType) {
                        $scope.chart.options.data[0].type = chartType;
                        $scope.chart.render(); //re-render the chart to display the new layout
                    };
                },
                function error(response) {
                    if (response.error instanceof Error) {
                        console.log("client-side");
                    } else {
                        console.log("server-side");
                    }
                });
    };

    $scope.chartEmployeParService();

});

app.controller('modalEmployeController',function ($scope,$modalInstance,uId) {
    $scope.employe_id  = uId;
});