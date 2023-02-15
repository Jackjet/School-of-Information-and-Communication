Array.prototype.contain = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val)
            return true;
    }
    return false;
}
Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
}

if (!('z012' in window)) window['z012'] = {}
z012.alert = function (msg, callback) {
    if(callback){
        bootbox.alert(msg, callback);
    }
    else {
        bootbox.alert(msg);
    }
}

z012.confirm = function (msg, callback) {
    if (callback) {
        bootbox.confirm(msg, callback);
    }
    else {
        bootbox.confirm(msg);
    }
}

z012.prompt = function (msg, callback) {
    if (callback) {
        bootbox.prompt(msg, callback);
    }
    else {
        bootbox.prompt(msg);
    }
}