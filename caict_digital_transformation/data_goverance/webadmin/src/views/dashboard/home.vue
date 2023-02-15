<template>
  <div class="dashboard-container">
    <div class="main" v-loading.lock="fullscreenLoading">
      <div class="containn">
        <h3 class="pdl10">关键数据质量指标</h3>
      </div>
      <div class="flex contain">
        <div class="child" v-for="(item, index) in datas" :key='index'>
          <p class='center'>{{ item.name }}</p>
          <div class="flex">
            <div class="childd">
              <board :value='item.accuracy'></board>
            </div>
            <div class="childdd">
              总共执行：{{ item.count }}条<br/>
              不符合规则：{{ item.errorCount }}条<br/>
              准确率：{{ item.accuracy }}%
            </div>
          </div>
          <p class='center'>检查评分：{{ item.date }}</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {
  dataQualityIndex,
  findByCurrentUser,
  mainMenuTree,
  verifyUserRole
} from '@/api/user'
import {sessionStorageGet} from '@/utils/storage'
import board from '@/components/echarts/board'
import list from '@/components/list/list'

export default {
  components: {board, list},
  data() {
    return {
      List: [
        {
          img: require('@/assets/image/zhishi.png')
        },
        {
          img: require('@/assets/image/data.png')
        },
        {
          img: require('@/assets/image/yunwei.png')
        },
        {
          img: require('@/assets/image/base.png')
        },
        {
          img: require('@/assets/image/userxw.png')
        },
        {
          img: require('@/assets/image/mokuai.png')
        },
        {
          img: require('@/assets/image/zidonghua.png')
        },
        {
          img: require('@/assets/image/shujk.png')
        },
        {
          img: require('@/assets/image/yuanchen.png')
        }
      ],
      datas: [],
      activeName: '最新提交工单',
      data: {
        title: '最新提交工单',
        type: '',
        total: '',
        dataLeft: [],
        dataRight: []
      },
      fullscreenLoading: false,
    }
  },
  created() {
    this.getMenuTree()
    this.getList()
  },
  methods: {
    async getMenuTree() {
      const data = {}
      const that = this
      try {
        const res = await mainMenuTree(data)
        if (res.data.code === 1) {
          if (res.data.data.length > 0) {
            res.data.data.map(function (v, k) {
              let id = parseInt(v.entity.id) - 2
              that.listMain.push(that.List[id])
            })
          }
          // this.List.push({ img: require('@/assets/image/zhishi.png')})
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async getList() {
      const data = {}
      const that = this
      this.fullscreenLoading = true
      try {
        const res = await dataQualityIndex(data)
        if (res.data.code === 1) {
          //  res.data.data.map(function(v, k){
          // 	 v.count = Math.round(Math.random() * 100)
          // 	 v.accuracy =  v.count
          //  })
          this.datas = res.data.data
        }
        this.fullscreenLoading = false
      } catch (even) {
        this.$message.error(even.msg)
        this.fullscreenLoading = false
      }
    },
    async findByCurrent(status) {
      const data = {
        page: 1,
        size: 10,
      }
      const that = this
      try {
        const res = await findByCurrentUser(data)
        that.data.type = 1
        that.data.dataRight = []
        that.data.dataLeft = []
        if (res.data.code === 1 && res.data.data.content.length > 0) {
          res.data.data.content.map(function (v, k) {
            // if(k%2 === 0){
            that.data.dataLeft.push({id: v.ruleId, name: v.ruleName, contentTIme: v.createTime})
            // }else{
            // 	that.data.dataRight.push({name: v.ruleName, contentTIme: v.createTime})
            // }
          })
          that.data.total = res.data.data.totalElements
          // this.data.dataRight = res.data.data.content
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    add: function () {
      this.$refs.add.initial('system')
    },
    go() {
      this.$router.push({path: '/'})
    }
  }
}
</script>
<style lang="scss" scoped>

.dashboard {
  &-container {
    // margin: 2px;
    // padding: 0 10px;
    padding: 15px 0 30px 0;
    height: 100%;
    min-height: 100vh;
    background-color: #f6f6f6;
    // background-image: url(../../assets/image/index.png);
    // height: 100vh;
    // width: 90%;
    // margin: 0 auto;
    position: relative;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }

}

img {
  display: block;
  width: 100%;
  // height: 40%;
  max-height: 100%;
  max-width: 100%;
}

.flex {
  display: flex;
  display: -webkit-flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-content: flex-start;
}

.h100 {
  min-height: 100vh;
}

.child {
  box-sizing: border-box;
  // background-color: white;
  flex: 0 0 24%;
  background: white;
  margin: 0.2vw;
  box-shadow: 0 6px 15px rgba(36, 37, 38, 0.1);
  border-radius: 5px;
  overflow: hidden;
  transition: box-shadow 0.25s ease, transform 0.25s ease;

  // height: 50px;
  // border: 1px solid red;
}

.childd {
  box-sizing: border-box;
  // background-color: white;
  // flex: 0 0 49%;
  // flex-grow: 1;
  display: flex;
  background: #fff;
  // flex-shrink: 0;
  // flex-grow: 2;
  flex: 1.2;
  text-align: end;
  // height: 50px;
  // border: 1px solid red;
}

.childdd {
  box-sizing: border-box;
  // background-color: white;
  // flex: 0 0 49.5%;
  flex: 1;
  padding-right: 8px;
  line-height: 1.5;
  // flex-grow: 1;
  // flex-shrink: 0;
  background: #fff;
  display: flex;
  align-items: center;
  flex-direction: row;
  // margin: 4px;
  // height: 50px;
  // border: 1px solid red;
}

.center {
  text-align: center;
}

.dashboard-container {
  .main {
    width: 95%;
    margin: 0 auto;
    // background: #fff;
  }

  .pdl10 {
    margin: 0 0 10px 3px;
    display: inline-block;
    padding: 5px 10px 5px 10px;
    // line-height: 30px;
    // background: white;
    // box-shadow: 0 6px 15px rgba(36, 37, 38, 0.1);
    // border-radius: 5px;
    // overflow: hidden;
    // transition: box-shadow 0.25s ease, transform 0.25s ease;
  }

  .mb20 {
    margin-bottom: 40px;
  }

  .contain {
    // background: #fff;
  }

  .bgColor {
    padding: 5px;
    background: #fff;
    margin-top: 15px;
  }

  .containn {
    // background: #fff;
    p {
      margin: 0;
    }
  }

  .box {
    position: absolute;
    z-index: 5;
    background: #fff;
    width: 80%;
    height: 64%;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%); /*自己的50% */
    border-radius: 5px;
    padding: 20px;

    .item {
      box-sizing: border-box;
      // flex: 0 0 33%;
      flex: 0 0 1;
      // width: 30%;
      height: 30%;
      // background-image: url(../../assets/image/zhishi.png);

    }
  }

  .top {
    width: 100%;
    height: 50%;
    background: url(../../assets/image/bank.png);
    background-size: 100% 100%;
  }

  .item {
    cursor: pointer;
  }
}

</style>
