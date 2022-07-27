<template>
  <div class="user-body">
    <div class="user-head">
      <el-button plain size="small">插入</el-button>
    </div>
    <div class="user-main">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="name" label="姓名" width="180" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="memberId" label="培训编号" />
        <el-table-column prop="teamId" label="竞赛编号" />
        <el-table-column label="操作" width="230">
          <template #default="scope">
            <el-button size="small" @click="edit(scope.row)">修改</el-button>
            <el-button size="small" type="danger">删除</el-button>
            <el-button size="small">作业情况</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pages">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        hide-on-single-page
        :page-size="20"
      />
    </div>
  </div>
  <el-dialog v-model="addOrEditFlag" width="400px">
    <add-or-edit-dialog
      v-if="addOrEditFlag"
      :userInfo="userInfo"
      :type="type"
    ></add-or-edit-dialog>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import AddOrEditDialog from "@/components/AddOrEditDialog.vue";
import { fuzzyQuery } from "@/api/request";
export default defineComponent({
  components: { AddOrEditDialog },
  setup() {
    const tableData = ref<any[]>([]);
    const total = ref(0);
    const addOrEditFlag = ref(false);
    const userInfo = ref<any>();
    const type = ref("add");

    const edit = (val: any) => {
      console.log(val);
      addOrEditFlag.value = true;
      userInfo.value = val;
      type.value = "edit";
    };

    onMounted(async () => {
      const data = await fuzzyQuery({
        type: "",
        keyWord: "",
        page: 0,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        console.log(data);
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
      }
    });

    return {
      tableData,
      total,
      edit,
      addOrEditFlag,
      userInfo,
      type,
    };
  },
});
</script>

<style lang="scss" scoped>
.user-body {
  height: 100%;
  position: relative;
  .user-head {
    height: 40px;
    background: rgba($color: #545c64, $alpha: 0.5);
    padding: 0 20px;
    .el-button {
      margin-top: 7px;
    }
  }
  .user-main {
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