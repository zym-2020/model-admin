<template>
  <div class="list-body">
    <div class="list-head">
      <div>
        <el-input
          v-model="inputValue"
          placeholder="请输入学员编号"
          size="small"
        />
      </div>
      <el-button size="small" type="primary" @click="query">查询</el-button>
      <el-select
        v-model="numberValue"
        size="small"
        placeholder="作业完成数"
        @change="changeHandle"
      >
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item"
          :value="item"
        />
      </el-select>
    </div>
    <div style="padding: 20px 30px" v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
    <div class="list-main" v-if="!skeletonFlag">
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="UserId" />
        <el-table-column prop="memberId" label="培训编号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="finishedCount" label="作业数" width="100" />
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
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { InfoFilled } from "@element-plus/icons-vue";
import { getList } from "@/api/request";
export default defineComponent({
  setup() {
    const inputValue = ref("");
    const keyword = ref("");
    const tableData = ref<any[]>([]);
    const total = ref(0);
    const numberValue = ref(1);
    const options = [1, 2, 3, 4];
    const skeletonFlag = ref(true);
    const currentPage = ref(1);

    const currentChange = async () => {
      const data = await getList({
        number: numberValue.value,
        keyword: keyword.value,
        page: currentPage.value - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
      }
    };

    const changeHandle = async () => {
      const data = await getList({
        number: numberValue.value,
        keyword: keyword.value,
        page: 0,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        currentPage.value = 1;
      }
    };

    const query = async () => {
      keyword.value = inputValue.value;
      const data = await getList({
        number: numberValue.value,
        keyword: keyword.value,
        page: 0,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        currentPage.value = 1;
      }
    };

    onMounted(async () => {
      const data = await getList({
        number: numberValue.value,
        keyword: keyword.value,
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
      tableData,
      numberValue,
      options,
      skeletonFlag,
      total,
      currentPage,
      changeHandle,
      currentChange,
      inputValue,
      query,
    };
  },
});
</script>

<style lang="scss" scoped>
.list-body {
  height: 100%;
  .list-head {
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
  .list-main {
    padding: 20px 30px;
  }
}
</style>