<template>
  <div class="cert-body">
    <div class="cert-head">
      <el-button size="small" type="primary" @click="addCertClick"
        ><el-icon><Plus /></el-icon
      ></el-button>
    </div>
    <div style="padding: 20px 30px" v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
    <div class="cert-main" v-if="!skeletonFlag">
      <el-table :data="tableData" border>
        <el-table-column prop="userId" label="UserId" />
        <el-table-column prop="number" label="文件编号" />
        <el-table-column prop="type" label="证书类型" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="edit(scope.row)">修改</el-button>
            <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              :icon="InfoFilled"
              icon-color="#626AEF"
              title="确定删除该用户信息?"
              @confirm="delClick(scope.row)"
            >
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pages" v-if="!skeletonFlag">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total"
        v-model:current-page="currentPage"
        hide-on-single-page
        :page-size="20"
        @current-change="currentChange"
      />
    </div>
  </div>
  <el-dialog v-model="addCertFlag" width="450px">
    <add-cert
      v-if="addCertFlag"
      @returnAddCert="returnAddCert"
      @returnUpdateCert="returnUpdateCert"
      :certForm="certForm"
      :certType="certType"
    ></add-cert>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { InfoFilled } from "@element-plus/icons-vue";
import AddCert from "@/components/AddCert.vue";
import { addCert, certfuzzyQuery, delCert } from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { AddCert },
  setup() {
    const skeletonFlag = ref(true);
    const addCertFlag = ref(false);
    const tableData = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const type = ref("");
    const keyWord = ref("");
    const certForm = ref<any>(undefined);
    const certType = ref("");

    const addCertClick = () => {
      certType.value = "add";
      addCertFlag.value = true;
    };

    const edit = (val: any) => {
      console.log(val);
      certType.value = "update";
      certForm.value = val;
      addCertFlag.value = true;
    };

    const delClick = async (val: any) => {
      const data = await delCert({
        id: val.id,
        type: type.value,
        keyWord: keyWord.value,
        page: currentPage.value - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        notice("success", "成功", "删除成功");
      }
    };

    const currentChange = async (page: number) => {
      const data = await certfuzzyQuery({
        type: type.value,
        keyWord: keyWord.value,
        page: page - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
      }
    };

    const returnAddCert = async (val: any) => {
      console.log(val);
      const data = await addCert(val, 0, 20);
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        currentPage.value = 1;
        notice("success", "成功", "添加成功");
      }
      addCertFlag.value = false;
    };

    const returnUpdateCert = (val: any) => {
      for (let i = 0; i < tableData.value.length; i++) {
        if (tableData.value[i].id === certForm.value.id) {
          tableData.value[i].type = val.type;
          tableData.value[i].userId = val.userId;
          tableData.value[i].number = val.number;
          break;
        }
      }
      addCertFlag.value = false;
    };

    onMounted(async () => {
      const data = await certfuzzyQuery({
        type: "",
        keyWord: "",
        page: 0,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
      }
      skeletonFlag.value = false;
    });

    return {
      InfoFilled,
      skeletonFlag,
      addCertFlag,
      tableData,
      returnAddCert,
      currentPage,
      currentChange,
      total,
      type,
      keyWord,
      certForm,
      certType,
      addCertClick,
      edit,
      returnUpdateCert,
      delClick
    };
  },
});
</script>

<style lang="scss" scoped>
.cert-body {
  height: 100%;
  .cert-head {
    height: 40px;
    background: rgba($color: #545c64, $alpha: 0.5);
    padding: 0 20px;
    .el-button {
      margin-top: 7px;
    }
  }
  .cert-main {
    padding: 20px 30px;
  }
  .pages {
    position: absolute;
    width: 400px;
    bottom: 20px;
    left: calc(50% - 200px);
    display: flex;
    justify-content: center;
  }
}
</style>