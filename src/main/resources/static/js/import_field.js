var demo_str = `
    <div>
      <el-dialog title="建表SQL" :visible.sync="dialogFormVisible">
          <el-form :model="form">
            <el-form-item :label-width="formLabelWidth">
                <template slot="label">
                    <el-col :span="24">
                    请输入建表SQL
                    <el-button >导入实例</el-button>
                    </el-col>
                </template>
            </el-form-item>
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
            <el-button @click="dialogFormVisible = false">取 消</el-button>
          </div>
        </el-dialog>
    </div>
  `
var demoComponent = Vue.extend({
    template:demo_str ,
    props:['info'],  //开心的收下爹地给的旺仔牛奶
    data(){
        return {
            dialogFormVisible: false,
            form: {
                name: '',
                region: '',
                date1: '',
                date2: '',
                delivery: false,
                type: [],
                resource: '',
                desc: ''
            },
            formLabelWidth: '120px'
        }
    },
    methods: {
        handleClick() {
            this.$message({
                message: 'Button clicked!',
                type: 'success'
            });
        }
    }
})

Vue.component('import_field',demoComponent)