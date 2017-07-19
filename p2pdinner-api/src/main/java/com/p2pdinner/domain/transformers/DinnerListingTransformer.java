package com.p2pdinner.domain.transformers;

import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.vo.DinnerListingVO;
import org.apache.commons.collections.Transformer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;

public class DinnerListingTransformer implements Transformer {

	public Object transform(Object object) {
		if (object != null && object instanceof DinnerListing) {
			DinnerListing dinnerListing = (DinnerListing) object;
			DinnerListingVO dinnerListingVO = new DinnerListingVO();
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
			dinnerListingVO.setStartTime(new DateTime(dinnerListing.getStartTime().getTime()).toString(dateTimeFormatter));
			dinnerListingVO.setEndTime(new DateTime(dinnerListing.getEndTime().getTime()).toString(dateTimeFormatter));
			dinnerListingVO.setCloseTime(new DateTime(dinnerListing.getCloseTime().getTime()).toString(dateTimeFormatter));
			dinnerListingVO.setMenuItemId(dinnerListing.getMenuItem().getId());
			dinnerListingVO.setDinnerListingId(dinnerListing.getId());
			BeanUtils.copyProperties(dinnerListing, dinnerListingVO, "startTime", "endTime", "closeTime");
			return dinnerListingVO;
		}
		return null;
	}

}
