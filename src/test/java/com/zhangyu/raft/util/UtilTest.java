package com.zhangyu.raft.util;

import com.zhangyu.raft.entity.AppendLogRequestParam;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
    private AppendLogRequestParam param;

    @Test
    public void P2RTest() {
        param = new AppendLogRequestParam();
        param.setPrevLogIndex(1);
        param.setPrevLogTerm(1);
        param.setLeadId("1");
        param.setTerm(1);
        AppendLogRequestParam res = ParamToRequest.requestToParam(ParamToRequest.paramToRequest(param));
        Assert.assertNull(res.getLogEntry());
    }
}
