<template>
  <div class="user-body">
    <div class="user-head">
      <el-select v-model="typeValue" size="small" placeholder="选择">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <div>
        <el-input
          v-model="inputValue"
          placeholder="请输入关键词"
          size="small"
          :disabled="typeValue === ''"
        />
      </div>
      <el-button size="small" type="primary" @click="query">查询</el-button>
      <el-button plain size="small" @click="addClick">插入</el-button>
    </div>
    <div class="user-main" v-if="!skeletonFlag">
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="UserId" />
        <el-table-column prop="name" label="姓名" width="180" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="memberId" label="培训编号" />
        <el-table-column prop="teamId" label="竞赛编号" />
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
    <div style="padding: 20px 30px" v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
  </div>
  <el-dialog v-model="addOrEditFlag" width="400px">
    <add-or-edit-dialog
      v-if="addOrEditFlag"
      :userInfo="userInfo"
      :type="type"
      @returnData="returnData"
    ></add-or-edit-dialog>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import AddOrEditDialog from "@/components/AddOrEditDialog.vue";
import { fuzzyQuery, addUserInfo, delUser } from "@/api/request";
import { notice } from "@/utils/notice";
import { InfoFilled } from "@element-plus/icons-vue";
export default defineComponent({
  components: { AddOrEditDialog },
  setup() {
    const skeletonFlag = ref(true);
    const tableData = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const addOrEditFlag = ref(false);
    const userInfo = ref<any>();
    const type = ref("add");
    const inputValue = ref("");
    const typeValue = ref("");
    const keyWord = ref("");

    const options = [
      {
        value: "name",
        label: "姓名",
      },
      {
        value: "email",
        label: "邮箱",
      },
      {
        value: "memberId",
        label: "培训编号",
      },
      {
        value: "teamId",
        label: "竞赛编号",
      },
    ];

    const edit = (val: any) => {
      console.log(val);
      addOrEditFlag.value = true;
      userInfo.value = val;
      type.value = "edit";
    };

    const addClick = () => {
      type.value = "add";
      addOrEditFlag.value = true;
    };

    const delClick = async (val: any) => {
      const data = await delUser({
        id: val.id,
        keyWord: keyWord.value,
        type: typeValue.value,
        page: currentPage.value - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        notice("success", "成功", "删除成功");
      }
    };

    const returnData = async (val: any) => {
      if (val.flag) {
        for (let i = 0; i < tableData.value.length; i++) {
          if (tableData.value[i].id === userInfo.value.id) {
            tableData.value[i].name = val.data.name;
            tableData.value[i].email = val.data.email;
            tableData.value[i].memberId = val.data.memberId;
            tableData.value[i].teamId = val.data.teamId;
            notice("success", "成功", "修改成功");
            addOrEditFlag.value = false;
          }
        }
      } else {
        const data = await addUserInfo(val.data, 0, 20);
        if (data != null) {
          if ((data as any).code === 0) {
            tableData.value = data.data.content;
            total.value = data.data.totalElements;
            notice("success", "成功", "添加成功");
            currentPage.value = 1;
            inputValue.value = "";
            keyWord.value = "";
            typeValue.value = "";
            addOrEditFlag.value = false;
          } else {
            notice("warning", "警告", (data as any).msg);
          }
        }
      }
    };

    const currentChange = async (val: number) => {
      console.log(val);
      const data = await fuzzyQuery({
        type: typeValue.value,
        keyWord: keyWord.value,
        page: val - 1,
        size: 20,
      });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.content;
        total.value = data.data.totalElements;
        inputValue.value = keyWord.value;
      }
    };

    const query = async () => {
      keyWord.value = inputValue.value;
      const data = await fuzzyQuery({
        type: typeValue.value,
        keyWord: keyWord.value,
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
      skeletonFlag.value = true;
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
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      InfoFilled,
      tableData,
      total,
      currentPage,
      options,
      edit,
      addClick,
      addOrEditFlag,
      userInfo,
      type,
      returnData,
      delClick,
      typeValue,
      inputValue,
      currentChange,
      query,
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
    display: flex;
    .el-select {
      margin-top: 7px;
      margin-right: 10px;
      width: 100px;
    }
    .el-input {
      margin-top: 7px;
      width: 200px;
      margin-right: 10px;
    }
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