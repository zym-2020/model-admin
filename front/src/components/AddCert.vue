<template>
  <el-skeleton :rows="5" animated v-if="skeletonFlag" />
  <div v-else>
    <el-form label-width="70px" :model="form">
      <el-form-item label="证书类型">
        <el-select v-model="form.type" placeholder="选择类型">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户Id">
        <el-input v-model="form.userId" placeholder="UserId">
          <template #append>
            <el-button :icon="Search" @click="checkClick">检验</el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="文件编号">
        <el-input v-model="form.number" placeholder="文件编号" />
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button type="primary" plain @click="commit" v-if="!updateFlag"
        >确定</el-button
      >
      <el-button type="success" plain @click="update" v-else>修改</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import { checkUserId, updateCert } from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  props: {
    certForm: {
      type: Object,
    },
    certType: {
      type: String,
    },
  },
  emits: ["returnAddCert", "returnUpdateCert"],
  setup(props, context) {
    const skeletonFlag = ref(true);
    const updateFlag = ref(false);
    const options = [
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

    const form = reactive({
      type: "",
      userId: "",
      number: "",
    });

    const checkClick = async () => {
      if (form.userId === "") {
        notice("warning", "警告", "请先填写UserId!");
        return;
      }
      const data = await checkUserId(form.userId);
      if (data != null) {
        if ((data as any).code === 0) {
          notice("success", "成功", `存在用户：${data.data}`);
        } else {
          notice("warning", "警告", "不存在该用户");
        }
      }
    };

    const update = async () => {
      const data = await updateCert({
        id: props.certForm?.id,
        userId: form.userId,
        type: form.type,
        number: form.number,
      });
      if (data != null && (data as any).code === 0) {
        context.emit("returnUpdateCert", form);
      }
    };

    const commit = () => {
      if (form.type != "" && form.userId != "" && form.number != "") {
        context.emit("returnAddCert", form);
      } else {
        notice("warning", "警告", "请先完善信息");
      }
    };

    onMounted(() => {
      if (props.certType === "update") {
        form.type = props.certForm?.type;
        form.userId = props.certForm?.userId;
        form.number = props.certForm?.number;
        updateFlag.value = true;
      }
      skeletonFlag.value = false;
    });

    return {
      options,
      form,
      Search,
      checkClick,
      commit,
      skeletonFlag,
      update,
      updateFlag,
    };
  },
});
</script>

<style lang="scss" scoped>
.btn {
  text-align: center;
}
</style>