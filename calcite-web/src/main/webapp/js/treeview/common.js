try { document.execCommand("BackgroundImageCache", false, true); } catch (e) { }
$(document).ready(function() {
    if ($.browser.msie) {
        var v = parseInt($.browser.version);
        $(document.body).addClass("ie ie" + v);
    }
});