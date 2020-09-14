/**
 * Async Navbar With Ajax
 * CopyRight 2020.1 by Hexu
 * Interface Like: [{ "id": "ident", "text": "title text", "icon": "icon", "href": "targetUrl.php", "hasChildren": 0 }]
 */
;layui.define(['element','jquery'],function(exports){
    'use strict';
    var $ = layui.jquery, element = layui.element, navroot = null, nodes=[], methods = {
        select: function(id) {
            if( nodes[id] ) {
                navroot.find('li,dd').removeClass('layui-this');
                nodes[id].addClass('layui-this').parents('li,dd').addClass('layui-nav-itemed');
                opts.onSelect(nodes[id].data('nodeItem'));
            }
        }
    }, internal = {
        init: function() {
            navroot = $(opts.element).addClass('layui-side layui-layout-left layui-nav-side')
                .css({'background-color':opts.background, width: opts.width });
            var node = $('<ul class="layui-nav layui-nav-tree"></ul>').attr('lay-shrink', opts.shrink?'all':'').appendTo( navroot );
            node.css('width','100%');
            internal.getData(null,node,0);
        },
        getData: function(id,node,level){
            id = id || null;
            $.ajax({
                url: opts.url,
                type: opts.type,
                cache: false,
                data: { id: id },
                dataType: 'json',
                success: function(res) {
                    $.each(res, function (index, val) {
                        /**
                         * @var val.hasChildren
                         */
                        val.hasChildren = ( val.hasChildren && val.hasChildren > 0 );
                        var sub = (id === null) ? $('<li class="layui-nav-item"></li>').appendTo(node) : $('<dd></dd>').appendTo(node);
                        sub.data('nodeItem', val);
                        nodes[val.id] = sub;
                        var a = $('<a href="javascript:void(0)"></a>').appendTo(sub);
                        a.css('margin-left', level * 12 );
                        if (val.icon) {
                            $('<i class="layui-icon"></i>').addClass(val.icon).appendTo(a);
                        }
                        $('<cite></cite>').text(val.text).appendTo(a);
                        if (val.hasChildren ) {
                            $('<span class="layui-nav-more"></span>').appendTo(a);
                            var dl = $('<dl class="layui-nav-child"></dl>').appendTo(sub);
                            if( opts.autoExpand ) {
                                internal.getData(val.id, dl, level + 1);
                                val.isExpended = true;
                            }
                        }
                        /**
                         * @var val.isExpended
                         */
                        a.on('click',function() {
                            if( val.hasChildren && ( ! val.isExpended )){
                                internal.getData(val.id, dl, level + 1 );
                                val.isExpended = true;
                            }
                            opts.onSelect(val);
                        });
                    });
                    element.render('nav');
                }
            });
        }
    }, opts = {};

    exports('hxNav',function(opt, params ) {
        if( typeof opt === 'string' ) {
            if( methods[ opt ] )
                methods[ opt ].call( this, params );
        }
        else {
            opts = $.extend({
                element: '',
               // width: 220,
                url: '',
                type: 'post',
                shrink: false,
                autoExpand: false,
                background: '#393D49',
                onSelect: function(node){}
            }, opt || {});
            internal.init.call( this );
        }
    });
});