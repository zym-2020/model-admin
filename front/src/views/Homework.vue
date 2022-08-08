<template>
  <div class="homework-body">
    <div class="homework-head">
      <div>
        <el-input
          v-model="inputValue"
          placeholder="请输入学员编号"
          size="small"
        />
      </div>
      <el-button size="small" type="primary" @click="query">查询</el-button>

      <el-select v-model="numberValue" size="small" placeholder="选择完成数">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item"
          :value="item"
        />
      </el-select>
      <el-button
        size="small"
        type="primary"
        :disabled="numberValue == ''"
        @click="downloadAllClick"
        >下载作业</el-button
      >
    </div>
    <div class="homework-main" v-if="!skeletonFlag">
      <el-table :data="tableData" border :row-class-name="tableRowClassName">
        <el-table-column prop="fileName" label="存储文件名" />
        <el-table-column prop="memberId" label="培训编号" />
        <el-table-column prop="number" label="作业编号">
          <template #default="scope">
            {{ getModelName(scope.row.number) }}
          </template>
        </el-table-column>
        <el-table-column prop="state" label="状态" width="100">
          <template #default="scope">
            <el-tag style="width: 50px" v-if="scope.row.state === 1">{{
              scope.row.state
            }}</el-tag>
            <el-tag style="width: 50px" v-else type="danger">{{
              scope.row.state
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template #default="scope">
            <el-button size="small" @click="updateClick(scope.row)"
              >修改</el-button
            >
            <el-button size="small" @click="downloadClick(scope.row)"
              >下载</el-button
            >
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
    <div style="padding: 20px 30px">
      <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    </div>
  </div>

  <el-dialog v-model="updateFlag" width="400px">
    <upload-homework
      :formValue="formValue"
      @returnUpdateData="returnUpdateData"
    ></upload-homework>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch } from "vue";
import {
  findHomeworkAll,
  delHomework,
  homeworkFuzzyQuery,
} from "@/api/request";
import { InfoFilled } from "@element-plus/icons-vue";
import UploadHomework from "@/components/UpdateHomework.vue";

export default defineComponent({
  components: { UploadHomework },
  setup() {
    const total = ref(0);
    const currentPage = ref(1);
    const skeletonFlag = ref(true);
    const updateFlag = ref(false);
    const tableData = ref<any[]>([]);
    const memberId = ref("");
    const inputValue = ref("");
    const numberValue = ref("");
    const formValue = ref<any>({
      name: "",
      memberId: "",
      number: "",
      state: "",
    });

    const options = ["1", "2", "3", "4"];

    const tableRowClassName = ({ row }: { row: any }) => {
      if (row.state === 1) {
        return "success-row";
      }
    };

    const updateClick = (val: any) => {
      formValue.value = val;
      updateFlag.value = true;
    };

    const downloadClick = (val: any) => {
      window.location.href = `http://localhost:7777/homework/download/${val.fileName}`;
    };

    const downloadAllClick = () => {
      if (numberValue.value != "") {
        window.location.href = `http://localhost:7777/homework/compressHomework/${numberValue.value}`;
      }
    };

    const query = async () => {
      memberId.value = inputValue.value;
      const data = await homeworkFuzzyQuery({
        memberId: memberId.value,
        page: currentPage.value - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        currentPage.value = 1;
      }
    };

    const delClick = async (val: any) => {
      console.log(val);
      const data = await delHomework({
        id: val.id,
        memberId: memberId.value,
        page: currentPage.value - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
      }
    };

    const currentChange = async (page: number) => {
      const data = await homeworkFuzzyQuery({
        memberId: memberId.value,
        page: page - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
      }
    };

    const getModelName = (number: string) => {
      if (number === "1") {
        return "GeoDetector模型";
      } else if (number === "2") {
        return "GeoSOS模型";
      } else if (number === "3") {
        return "HASM模型";
      } else {
        return "GWmodelS模型";
      }
    };

    const returnUpdateData = (val: any) => {
      for (let i = 0; i < tableData.value.length; i++) {
        if (val.id === tableData.value[i].id) {
          tableData.value[i].state = val.state;
        }
      }
      updateFlag.value = false;
    };

    onMounted(async () => {
      const data = await findHomeworkAll(0, 20);
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        skeletonFlag.value = false;
      }
    });

    return {
      skeletonFlag,
      tableData,
      InfoFilled,
      tableRowClassName,
      updateFlag,
      updateClick,
      delClick,
      formValue,
      getModelName,
      returnUpdateData,
      total,
      currentPage,
      currentChange,
      downloadClick,
      inputValue,
      query,
      numberValue,
      options,
      downloadAllClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.homework-body {
  height: 100%;
  .homework-head {
    height: 40px;
    background: rgba($color: #545c64, $alpha: 0.5);
    padding: 0 20px;
    display: flex;
    .el-input {
      margin-top: 7px;
      width: 200px;
      margin-right: 10px;
    }
    .el-button {
      margin-top: 7px;
    }
    .el-select {
      margin-top: 7px;
      margin-right: 10px;
      margin-left: 50px;
      width: 100px;
    }
  }
  .homework-main {
    padding: 20px 30px;
    .el-table {
      /deep/ .success-row {
        --el-table-tr-bg-color: var(--el-color-success-light-9);
      }
    }
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