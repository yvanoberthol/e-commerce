app.config(function ($stateProvider,$urlRouterProvider) {

    // acceder aux pages de liste
    $stateProvider.state("rest-employes",{
        url:"/rest-employes",
        templateUrl:"views/categories.html",
        controller: "employeController"
    });
    $stateProvider.state("rest-services",{
        url:"/rest-services",
        templateUrl:"views/services.html",
        controller: "serviceController"
    });
    $stateProvider.state("rest-serviceBudgetStat",{
        url:"/rest-serviceBudgetStat",
        templateUrl:"views/servicesStat.html",
        controller: "chartBudgetParServiceController"
    });
    $stateProvider.state("rest-serviceEmployeStat",{
        url:"/rest-serviceEmployeStat",
        templateUrl:"views/employesServiceStat.html",
        controller: "chartEmployeParServiceController"
    });

    $stateProvider.state("rest-users",{
        url:"/rest-users",
        templateUrl:"views/users.html",
        controller: "userController"
    });

    $stateProvider.state("rest-roles",{
        url:"/rest-roles",
        templateUrl:"views/roles.html",
        controller: "roleController"
    });

    $stateProvider.state("rest-userRoles",{
        url:"/rest-userRoles",
        templateUrl:"views/userRoles.html",
        controller: "userRoleController"
    });



    // acceder aux pages d'ajout
    $stateProvider.state("rest-ajoutEmploye",{
        url:"/rest-ajoutEmploye",
        templateUrl:"views/ajoutEmploye.html",
        controller: "employeController"
    });

    $stateProvider.state("rest-ajoutService",{
        url:"/rest-ajoutService",
        templateUrl:"views/ajoutService.html",
        controller: "serviceController"
    });

    $stateProvider.state("rest-ajoutUser",{
        url:"/rest-ajoutUser",
        templateUrl:"views/ajoutUser.html",
        controller: "userController"
    });

    $stateProvider.state("rest-ajoutRole",{
        url:"/rest-ajoutRole",
        templateUrl:"views/ajoutRole.html",
        controller: "roleController"
    });

    $stateProvider.state("rest-ajoutRoleToUser",{
        url:"/rest-ajoutRoleToUser",
        templateUrl:"views/ajoutUserRole.html",
        controller: "userRoleController"
    });

    //acceder aux pages de modification
    $stateProvider.state("rest-modifEmploye",{
        url:"/rest-modifEmploye",
        templateUrl:"views/modifEmploye.html",
        controller: "employeController"
    });

    $stateProvider.state("rest-modifService",{
        url:"/rest-modifService",
        templateUrl:"views/modifService.html",
        controller: "serviceController"
    });

    $stateProvider.state("rest-modifUser",{
        url:"/rest-modifUser",
        templateUrl:"views/modiftUser.html",
        controller: "userController"
    });



    $urlRouterProvider.otherwise("/rest-employes");
});