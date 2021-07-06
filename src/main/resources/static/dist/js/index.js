// selection switch
$('.to-select').click(function () {
    $(this).toggleClass('select-switch');
    if ($('a.select-switch').length == 0) {
        $(this).html('选择');
        $('.selected').removeClass('selected');
        $('.select-all').html('全选');
        $('.select-all').removeClass('select-all-on');
    } else
        $(this).html('取消选择');
});

// item selection
$('.block').click(function () {
    if ($('a.select-switch').length != 0) {
        $(this).toggleClass('selected');
        if ($('li.selected').length == 0) {
            $('.select-all').html('全选');
            $('.select-all').removeClass('select-all-on');
        } else {
            $('.select-all').html('取消全选');
            $('.select-all').addClass('select-all-on');
        }
    } else {
        let url = $(this).children('img').attr('src');
        console.log(url);

        window.open(url);
    }
    counter();
});

// all item selection
$('.select-all').click(function () {
    if ($('a.select-all-on').length == 0) {
        $('.to-select').html('取消选择');
        $('.to-select').addClass('select-switch');
    }
    //
    if ($('li.selected').length == 0)
        $('.block').addClass('selected');
    else
        $('.block').removeClass('selected');
    $(this).toggleClass('select-all-on')
    if ($('a.select-all-on').length == 0)
        $('.select-all').html('全选');
    else
        $('.select-all').html('取消全选');
    counter();
});

// number of selected items
function counter() {
    if ($('li.selected').length > 0)
        $('.send').addClass('selected');
    else
        $('.send').removeClass('selected');
    $('.send').attr('data-counter', $('li.selected').length);
}


// save
$('.save').click(function () {
    if ($('li.selected').length == 0)
        alert('点击[选择]按钮可选择图片');
    else {
        let objects = [];
        let object;
        $('.selected').each(function () {
            object = {
                'sourceBucket': $(this).children('img').attr('data'),
                'keyName': $(this).children('img').attr('value')
            };
            objects.push(object);
        });

        $.ajax({
            url: '/pages/action/save',
            type: 'post',
            data: JSON.stringify(objects),
            contentType: 'application/json;charset=UTF-8',
            xhrFields: {responseType: "blob"},
            success: function (data) {
                let url = window.URL.createObjectURL(new Blob([data], {type: "application/zip"}));
                const link = document.createElement('a');
                link.style.display = 'none';
                link.href = url;
                let filename = 'img_' + new Date().getFullYear() + (new Date().getMonth() + 1) + new Date().getDate();
                link.setAttribute('download', filename + '.zip');
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                window.URL.revokeObjectURL(link.href);
            }
        });
    }
});


// upload
$('.upload').click(function () {

});

// delete
$('.delete').click(function () {
    if ($('li.selected').length == 0)
        alert('点击[选择]按钮可选择图片');
    else {
        let objects = [];
        let object;
        $('.selected').each(function () {
            object = {
                'destinationBucket': 'recycle-bin',
                'sourceBucket': $(this).children('img').attr('data'),
                'keyName': $(this).children('img').attr('value')
            };
            objects.push(object);
        });

        $.ajax({
            url: '/pages/action/delete',
            data: JSON.stringify(objects),
            type: 'post',
            contentType: 'application/json;charset=UTF-8',
            success: function (data, status) {
                if (status == 'successs')
                    alert(status);
                else
                    alert(status)
                location.reload();
            }
        });
    }
});

// create video
$('.create_video').click(function () {
        $.ajax({
            url: '/action/create_video',
            type: 'get',
            success: function (data, status) {
                location.reload();
                alert(status);
            }
        });
});

// delete video
$('.delete_video').click(function () {
    $.ajax({
        url: '/action/delete_video',
        type: 'get',
        success: function (data, status) {
            location.reload();
            alert(data);
        }
    });
});





//delete completely
$('.delete_completely').click(function () {
    if ($('li.selected').length == 0)
        alert('点击[选择]按钮可选择图片');
    else {
        let objects = [];
        let object;
        $('.selected').each(function () {
            object = {
                'sourceBucket': $(this).children('img').attr('data'),
                'keyName': $(this).children('img').attr('value')
            };
            objects.push(object);
        });

        console.log(objects);
        $.ajax({
            url: '/pages/delete_completely',
            data: JSON.stringify(objects),
            type: 'post',
            contentType: 'application/json;charset=UTF-8',
            success: function (data, status) {
                if (status == 'success')
                    alert(status);
                else
                    alert(status)
                location.reload();
            }
        });
    }
});


// recover
$('.recover').click(function () {
    if ($('li.selected').length == 0)
        alert('点击[选择]按钮可选择图片');
    else {
        let objects = [];
        let object;

        $('.selected').each(function () {
            console.log($(this).children('img').attr('data'));
            object = {
                'sourceBucket': $(this).children('img').attr('data'),
                'destinationBucket': '',
                'keyName': $(this).children('img').attr('value')
            };
            objects.push(object);
        });

        $.ajax({
            url: '/pages/action/recover',
            data: JSON.stringify(objects),
            type: 'post',
            contentType: 'application/json;charset=UTF-8',
            success: function (data, status) {
                if (status == 'successs')
                    alert(status);
                else
                    alert(status)
                location.reload();
            }
        });
    }
});

// 检查空提交
function check() {
    let imgs = document.getElementsByName("upload_img")
    for (let i = 0; i < imgs.length; i++) {
        if (imgs[i].value == '') {
            alert("你还没有选择文件")
            return false;
        }
    }
    return true;
}


// 获取需要被active的导航栏信息
function activeEvent() {
    let activeType = $('#nav-mypic').attr('data');
    $('#'+activeType).addClass('active');
}

