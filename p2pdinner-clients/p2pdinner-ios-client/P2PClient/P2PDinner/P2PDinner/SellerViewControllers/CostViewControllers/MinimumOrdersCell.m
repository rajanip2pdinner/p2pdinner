//
//  MinimumOrdersCell.m
//  P2PDinner
//
//  Created by Selvam M on 4/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MinimumOrdersCell.h"
#import "ItemDetailsShared.h"
typedef enum{
    DecrementValue,
    IncrementValue
    
}ChangeValue;
@implementation MinimumOrdersCell
- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}
- (void)removeSelectedIndex:(UISegmentedControl *)segmentBtn{
    [segmentBtn setSelectedSegmentIndex:-1];
    [self.delegate updatedItems];
}
- (int)stringToInteger:(NSString *)stringValue{
    return [stringValue intValue];
}
- (void)changeValue:(ChangeValue)value{
    int oldValue;
    switch (value) {
        case DecrementValue:
        {
            if ([self stringToInteger:maximumOrdersLable.text]>5) {
                oldValue=[self stringToInteger:maximumOrdersLable.text];
                oldValue--;
                maximumOrdersLable.text=[NSString stringWithFormat:@"%d",oldValue];
            }
        }
            break;
        case IncrementValue:
        {
            if ([self stringToInteger:maximumOrdersLable.text]<100) {
                oldValue=[self stringToInteger:maximumOrdersLable.text];
                oldValue++;
                maximumOrdersLable.text=[NSString stringWithFormat:@"%d",oldValue];
            }
        }
        break;    }
    
}

- (IBAction)maximumNumberOfOrdersAction:(id)sender{
    UISegmentedControl *segment=(UISegmentedControl *)sender;
    if (segment.selectedSegmentIndex==0) {
        [self changeValue:DecrementValue];
    }else{
        [self changeValue:IncrementValue];
    }
    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.1];
}
- (void)setMaximuOrdersValue:(NSNumber *)value{
    maximumOrdersLable.text=[NSString stringWithFormat:@"%ld",(long)[value integerValue]];
}
- (NSNumber *)getMaximuOrdersValue{
    return [NSNumber numberWithDouble:[maximumOrdersLable.text doubleValue]];
}

@end
