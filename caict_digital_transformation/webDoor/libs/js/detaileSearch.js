$(function () {
    let flag = false;
    $('#input_btn').on('click', function () {
        flag = !flag;
        if (flag) {
            $('.input-text').css({ display: 'block' });
            $('.search-input-style').css({ border: '1px solid #eee', background: '#0546AE' });
            $('.input-text').animate({ width: '300px', height: '30px' });
            $('.search-input').animate({ padding: '0 5px' })
        } else {
            $('.input-text').css({ display: 'none' });
            $('.search-input-style').css({ border: 'none', background: 'none' });
            $('.search-input').animate({ padding: '0' })
        }
    })
})