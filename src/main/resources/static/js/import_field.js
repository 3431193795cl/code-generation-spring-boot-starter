var demo_str = `
    <div>
      <el-dialog width="36%" title="建表SQL" :visible.sync="dialogFormVisible">
          <el-form :model="form">
            <el-form-item :label-width="formLabelWidth">
                <template slot="label">
                    <el-col :span="24">
                    <a>请输入建表SQL</a>
                    </el-col>
                </template>
                <el-button  size="mini" @click="importInstance">导入实例</el-button>
            </el-form-item>
            <el-input type="textarea" :rows="20" v-model="form.sql" autocomplete="off"></el-input>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="closeDialog">取 消</el-button>
          </div>
        </el-dialog>
    </div>
  `
var demoComponent = Vue.extend({
    template: demo_str,
    props: ['info'],  //开心的收下爹地给的旺仔牛奶
    data() {
        return {
            dialogFormVisible: false,
            form: {
                sql: '',
            },
            formLabelWidth: '120px',
            default_sql: `
CREATE TABLE 'sys_user_info' (
  'user_id' int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  'user_name' varchar(255) NOT NULL COMMENT '用户名',
  'status' tinyint(1) NOT NULL COMMENT '状态',
  'create_time' datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ('user_id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';
            `
        }
    },
    methods: {
        importInstance() {
            this.form.sql = this.default_sql
        },
        submitForm() {
            this.$emit('submit', this.form.sql)
            this.form.sql = ''
            this.dialogFormVisible = false
        },
        closeDialog() {
            this.form.sql = ''
            this.dialogFormVisible = false
        }
    }
})

Vue.component('import_field', demoComponent)