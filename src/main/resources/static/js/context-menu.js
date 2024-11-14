var contextMenu = `
        <div v-show="menuShow" class="tree_rightmenu">
              <el-menu class="el-menu-vertical-demo" @select="selectMenuNode" >
                <el-menu-item id="menuitem" index="1" style="font-size: 12px;">
                  <span slot="title">查 看 DDL</span>
                </el-menu-item>
                <el-menu-item id="menuitem" index="2" style="font-size: 12px;">
                  <span slot="title">查 看 表</span>
                </el-menu-item>
                <el-menu-item index="3" style="font-size: 12px;">
                  <span slot="title">新 建 表</span>
                </el-menu-item>
                <el-menu-item index="4" style="font-size: 12px;">
                  <span slot="title">删 除 表</span>
                </el-menu-item>
              </el-menu>
         </div>
`
// context-menu.js
Vue.component('context-menu', {
    template: contextMenu,
    data() {
        return {
            menuShow: false,
            table_name: '',
        }
    },
    methods: {
        selectMenuNode(e){
            console.log(e)
        }
    }
});
