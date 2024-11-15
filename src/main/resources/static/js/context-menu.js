var contextMenu = `
        <div v-show="menuShow" class="tree_rightmenu">
              <el-menu class="el-menu-vertical-demo" @select="selectMenuNode" >
                <el-menu-item v-for="(item, index) in options" :key="item.value" :index="item.value" style="font-size: 12px;padding-left: 30px;padding-right: 30px;">
                    <span slot="title">{{item.label}}</span>
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
            options:[]
        }
    },
    methods: {
        selectMenuNode(e) {
            console.log(e)
            switch (e) {
                case '1':
                    this.getTableDDL(this.table_name)
                    break;
                case '2':

                    break;
                case '3':
                    self.location="/code/generate.html";
                    break;
                case '4':
                    this.$confirm('此操作将永久删除该数据表, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.removeTable(this.table_name)
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });

                    break;
            }

        },
        getTableDDL(tableName) {
            fetch("/operate/tableDDL", {
                method: 'POST', // 或者 'PUT'，取决于你的 API 要求
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json())
                .then(data => {
                    console.log(data)
                    this.data = data.data
                })
                .catch((error) => {
                    console.error('Error:', error);
                })
        },
        removeTable(tableName) {
            let that = this
            fetch(`/operate/removeTable/${tableName.label}`, {
                method: 'DELETE', // 或者 'PUT'，取决于你的 API 要求
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                }
            }).then(response => response.json())
                .then(data => {
                    console.log(data)
                    that.$message({
                        showClose: true,
                        message: '删除成功!',
                        type: 'success'
                    });
                    console.log(data.data)
                    if (data.data) {
                        that.$emit('del-table')
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                })
        },
    }
});
