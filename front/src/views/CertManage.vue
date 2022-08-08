<template>
  <div class="cert-body">
    <div class="cert-head">
      <el-button size="small" type="primary" @click="addCertClick"
        ><el-icon><Plus /></el-icon
      ></el-button>
      <el-select v-model="type" size="small">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <div class="input">
        <el-input
          v-model="inputValue"
          placeholder="请输入UserId"
          size="small"
        />
      </div>
      <el-button size="small" @click="query">查询</el-button>
      <div class="operate">
        <el-popconfirm
          confirm-button-text="确定"
          cancel-button-text="取消"
          :icon="InfoFilled"
          icon-color="#626AEF"
          title="导入名单将覆盖当前所有数据，是否确定导入？"
          @confirm="importListClick"
        >
          <template #reference>
            <el-button size="small">导入名单</el-button>
          </template>
        </el-popconfirm>

        <el-button size="small">批量删除</el-button>
      </div>
    </div>
    <div style="padding: 20px 30px" v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
    <div class="cert-main" v-if="!skeletonFlag">
      <el-table :data="tableData" border>
        <el-table-column prop="userId" label="UserId" />
        <el-table-column prop="number" label="文件编号" />
        <el-table-column prop="type" label="证书类型" />
        <el-table-column label="证书是否存在" width="150">
          <template #default="scope">
            <el-tag style="width: 50px" v-if="scope.row.fileState === 1">{{
              1
            }}</el-tag>
            <el-tag style="width: 50px" v-else type="danger">{{ 0 }}</el-tag>
          </template>
        </el-table-column>
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
  <el-dialog v-model="importListFlag" width="200px" title="选择作业数">
    <import-list @returnNumber="returnNumber"></import-list>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { InfoFilled } from "@element-plus/icons-vue";
import AddCert from "@/components/AddCert.vue";
import ImportList from "@/components/ImportList.vue";
import {
  addCert,
  certfuzzyQuery,
  delCert,
  importList,
  checkFile,
} from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { AddCert, ImportList },
  setup() {
    const skeletonFlag = ref(true);
    const addCertFlag = ref(false);
    const importListFlag = ref(false);
    const tableData = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const type = ref("");
    const inputValue = ref("");
    const keyWord = ref("");
    const certForm = ref<any>(undefined);
    const certType = ref("");
    const options = [
      {
        label: "All",
        value: "",
      },
      {
        label: "结业证书",
        value: "train",
      },
      {
        label: "应用竞赛证书",
        value: "apply",
      },
      {
        label: "开发竞赛证书",
        value: "develop",
      },
    ];

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
        tableData.value = data.data.list;
        total.value = data.data.total;
        notice("success", "成功", "删除成功");
      }
    };

    const query = async () => {
      keyWord.value = inputValue.value;
      const data = await certfuzzyQuery({
        type: type.value,
        keyWord: keyWord.value,
        page: 0,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.list;
        total.value = data.data.total;
        currentPage.value = 1;
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
        tableData.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const returnAddCert = async (val: any) => {
      console.log(val);
      const data = await addCert(val, 0, 20);
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.list;
        total.value = data.data.total;
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

    const returnNumber = async (val: number) => {
      console.log(val);
      const data = await importList(val);
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.list;
        total.value = data.data.total;
        currentPage.value = 1;
        notice("success", "成功", "导入成功")
      }
      importListFlag.value = false;
    };

    const importListClick = () => {
      importListFlag.value = true;
    };

    const fileState = async (fileNumber: string) => {
      if (fileNumber === undefined) {
        console.log(undefined);
      } else {
        const data = await checkFile(fileNumber);
        if (data.data != null && (data as any).code === 0) {
          return data.data;
        } else {
          return 0;
        }
      }
    };

    onMounted(async () => {
      const data = await certfuzzyQuery({
        type: "",
        keyWord: "",
        page: 0,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.list;
        total.value = data.data.total;
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
      delClick,
      options,
      inputValue,
      query,
      importListClick,
      importListFlag,
      returnNumber,
      fileState,
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
    display: flex;
    position: relative;
    .el-button {
      margin-top: 7px;
    }
    .el-select {
      margin-top: 7px;
      margin-right: 10px;
      margin-left: 10px;
      width: 100px;
    }
    .input {
      margin-top: 7px;
      margin-right: 10px;
    }
    .operate {
      position: absolute;
      right: 30px;
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