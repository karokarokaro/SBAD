localStorage.setItem('links', 'http://chainyiput.ctc.ru/validate_email.html?token=14f66800e2dae2d4e268f3215ced02d1&id=5810,http://chainyiput.ctc.ru/validate_email.html?token=6c12f7ba960eaeba15a8668b7811d204&id=5811,http://chainyiput.ctc.ru/validate_email.html?token=3fc304fd8aa575d524dc1922d8076db4&id=5812');
function doNext() {
    var links = localStorage.getItem("links");
    if (links != "") {
        var nextLink = links.split(",")[0];
        if (links.indexOf(",") != -1) {
            links = links.substr(links.indexOf(",")+1);
        } else {
            links = "";
        }
        localStorage.setItem("links", links);
        $.ajax({
            type: "GET",
            url: nextLink,
            success: function(json) {
                $('.btn.stretch.blue.vote_hero').click();
            },
            error: function(json) {
                var r = 4;
            },
            complete: function() {
                setTimeout(doNext, parseInt(Math.random()*8000)+2);
            }
        });
    }
}



var id = 114000;
function doNextReg() {
    var links = localStorage.getItem("emails");
    if (links != "") {
        var nextLink = links.split(",")[0];
        if (links.indexOf(",") != -1) {
            links = links.substr(links.indexOf(",")+1);
        } else {
            links = "";
        }
        localStorage.setItem("emails", links);
        $.ajax({
            type: "POST",
            url: 'http://chainyiput.ctc.ru/validate_email.html?id='+id,
            data: {
                token: 'f00ed107cf5597c751971ab3bdc28382',
                perform_register: 'perform_register',
                username: nextLink,
                reg_password1: nextLink,
                reg_password2: nextLink,
                email: nextLink,
                agreed: 1
            },
            success: function(json) {
                var r = 4;
            },
            error: function(json) {
                var r = 4;
            },
            complete: function() {
                ++id;
                setTimeout(doNextReg, parseInt(Math.random()*800)+200);
            }
        });
    }
}
doNextReg();
var ss = '';
for (var j = 0; j < 5000; ++j) {
    var s = '';
    for(var i = 0; i < 7; ++i) {
        s += String.fromCharCode('a'.charCodeAt(0)+parseInt(Math.random()*26));
    }
    s += parseInt(Math.random()*1000);
    ss += s+'\n';
}
