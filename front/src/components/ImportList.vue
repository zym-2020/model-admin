<template>
  <div>
    <el-select v-model="value" size="small">
      <el-option
        v-for="(item, index) in options"
        :key="index"
        :label="item"
        :value="item"
      />
    </el-select>
    <div class="btn">
      <el-button type="success" @click="clickHandle">确定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
export default defineComponent({
  emits: ["returnNumber"],
  setup(_, context) {
    const value = ref(3);
    const options = [1, 2, 3, 4];

    const clickHandle = () => {
      context.emit("returnNumber", value.value);
    };

    return {
      value,
      options,
      clickHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.btn {
  text-align: center;
  margin-top: 20px;
}
</style>