<template>
  <t-list style="margin-top: 0">
    <t-list-item @click="showNotify(notify.title,notify.content)" :key="notify.contestNotifyId" v-for="notify in notifyList" class="list-item">
      <t-list-item-meta :title="notify.title"
                        :description="getDescription(notify.content)"/>
      <br>
      <div style="margin-top: 2em">
        {{getDate(notify.createdDate)}}
      </div>
    </t-list-item>
  </t-list>
  <NotifyDialog @on-close="notifyDialog.visitable = false" :content="notifyDialog.content" :title="notifyDialog.title" :visitable="notifyDialog.visitable"/>
</template>

<script>

import {publishList} from "@/api/notify";
import {StringUtil} from "@/util/string.util";
import {getTimeStr} from "@/util/date.util";
import NotifyDialog from "@/page/contest/component/detail_page/dialog/NotifyDialog";

export default {
  name: "ContestNotify",
  components: {NotifyDialog},
  data() {
    return {
      notifyList: [],
      notifyDialog: {
        visitable: false,
        title: null,
        content: null
      }
    }
  },
  methods: {
    getDescription(str){
      return StringUtil.getStringPrefix(StringUtil.parseFromHtml(str),50)
    },
    getDate(date){
      return getTimeStr(date)
    },
    showNotify(title,content){
      this.notifyDialog.title = title
      this.notifyDialog.content = content
      this.notifyDialog.visitable = true
    }
  },
  mounted() {
    publishList(this.$route.params.contestId).then(resp => {
      this.notifyList = resp.data.data
    })
  }
}
</script>

<style scoped>
.list-item {
  cursor: pointer;
  height: 5em;
  /*border-top: #cccccc 1px solid;*/
  /*border-bottom: #cccccc 1px solid;*/
}

.list-item:hover {
  background-color: #cccccc;

}
</style>